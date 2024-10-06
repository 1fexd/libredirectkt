import {
    createPrinter,
    createProgram,
    EmitHint,
    factory,
    forEachChild,
    type FunctionDeclaration, ImportDeclaration, ImportSpecifier,
    isArrayLiteralExpression,
    isObjectLiteralExpression,
    isPropertyAssignment,
    isStringLiteral,
    isVariableStatement,
    NewLineKind,
    type Node,
    type SourceFile,
    type StringLiteral,
    SyntaxKind,
    type VariableStatement
} from "typescript";

function updateNode(node: Node, requiredName: string, sourceFile: SourceFile): FunctionDeclaration | VariableStatement | undefined {
    switch (node.kind) {
        case SyntaxKind.FunctionDeclaration: {
            const functionDeclaration = node as FunctionDeclaration;
            if (functionDeclaration.name?.text !== requiredName) return;

            return factory.updateFunctionDeclaration(functionDeclaration,
                [factory.createToken(SyntaxKind.ExportKeyword)],
                functionDeclaration.asteriskToken, functionDeclaration.name, functionDeclaration.typeParameters,
                functionDeclaration.parameters, functionDeclaration.type, functionDeclaration.body
            );
        }

        case SyntaxKind.VariableStatement: {
            const variableStatement = node as VariableStatement;
            const variableDeclaration = variableStatement.declarationList.declarations[0];
            const name = variableDeclaration.name.getText(sourceFile);
            if (name !== requiredName) return;

            return factory.updateVariableStatement(variableStatement,
                [factory.createToken(SyntaxKind.ExportKeyword)],
                variableStatement.declarationList
            );
        }
    }
}

export function extractNodes(sourceFile: SourceFile, extractNodes: Array<[string, SyntaxKind]>): (FunctionDeclaration | VariableStatement)[] {
    const nodes: (FunctionDeclaration | VariableStatement)[] = [];
    forEachChild(sourceFile, (node) => {
        for (let [name, kind] of extractNodes) {
            if (node.kind === kind) {
                const newNode = updateNode(node, name, sourceFile);
                if (newNode) {
                    nodes.push(newNode);
                }
            }
        }
    });

    return nodes;
}

export function toSourceFile(file: string): SourceFile {
    let program = createProgram([file], { allowJs: true });
    return program.getSourceFile(file);
}

export function getDefaultInstancesNode(nodes: (FunctionDeclaration | VariableStatement)[]): VariableStatement {
    return <VariableStatement>nodes.find(n => n.kind === SyntaxKind.VariableStatement)
}

export function extractDefaultInstances(node: Node | undefined) {
    const instances: { [frontend: string]: string[] } = {}
    if (!node || !isVariableStatement(node)) return instances;

    const obj = node.declarationList.declarations[0].initializer;
    if (obj && isObjectLiteralExpression(obj)) {
        for (let property of obj.properties) {
            if (isPropertyAssignment(property)) {
                const name = property.name;
                const initializer = property.initializer;

                if (isStringLiteral(name) && isArrayLiteralExpression(initializer)) {
                    instances[name.text] = initializer.elements
                        .filter(e => isStringLiteral(e))
                        .map(e => (e as StringLiteral).text);
                }
            }
        }
    }

    return instances;
}

function createImportSpecifier(identifier: string): ImportSpecifier {
    return factory.createImportSpecifier(false, undefined, factory.createIdentifier(identifier));
}

export function createImport(file: string, ...identifiers: string[]): ImportDeclaration {
    const specifiers = identifiers.map(createImportSpecifier);
    const namedImports = factory.createNamedImports(specifiers);

    const clause = factory.createImportClause(false, undefined, namedImports);
    return factory.createImportDeclaration(undefined, clause, factory.createStringLiteral(file));
}


export function printNodes(sourceFile: SourceFile, nodes: Node[], emitLine: (line: string) => void) {
    const printer = createPrinter({ newLine: NewLineKind.LineFeed });
    for (const node of nodes) {
        const line = printer.printNode(EmitHint.Unspecified, node, sourceFile);
        emitLine(line);
    }
}
