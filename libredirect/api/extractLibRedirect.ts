import {
    createPrinter,
    createProgram,
    EmitHint,
    factory,
    forEachChild,
    type FunctionDeclaration,
    isArrayLiteralExpression, isObjectLiteralExpression, isPropertyAssignment,
    isStringLiteral, isVariableDeclaration, isVariableStatement,
    NewLineKind,
    type Node,
    type SourceFile, type StringLiteral,
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

function extract(sourceFile: SourceFile, extractNodes: Array<[string, SyntaxKind]>): (FunctionDeclaration | VariableStatement)[] {
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

async function writeNodes(sourceFile: SourceFile, nodes: (FunctionDeclaration | VariableStatement)[], shims: string[], file: string) {
    const printer = createPrinter({ newLine: NewLineKind.LineFeed });
    const textNodes = nodes.map(n => printer.printNode(EmitHint.Unspecified, n, sourceFile));

    await Bun.write(file, [...shims, ...textNodes].join("\n"));
}

function getSourceFileFromFile(file: string) {
    let program = createProgram([file], { allowJs: true });
    return program.getSourceFile(file);
}

function extractDefaultInstances(node: Node | undefined) {
    const instances: { [frontend: string]: string[] }= {}
    if (!node || !isVariableStatement(node)) return instances;

    const obj = node.declarationList.declarations[0].initializer;
    if (obj && isObjectLiteralExpression(obj)) {
        for (let property of obj.properties) {
            if (isPropertyAssignment(property)) {
                const name = property.name;
                const initializer = property.initializer;

                if (isStringLiteral(name) && isArrayLiteralExpression(initializer)) {
                    const items = initializer.elements
                        .filter(e => isStringLiteral(e))
                        .map(e => (e as StringLiteral).text);

                    instances[name.text] = items;
                }
            }
        }
    }

    return instances;
}

const importShims = [`import { XMLHttpRequest } from "../shim/XMLHttpRequest.shim.ts";`]

const outputDir = "src/generated";
const sourceFile = getSourceFileFromFile("../browser_extension/src/assets/javascripts/services.js");

if (!sourceFile) {
    console.error("Failed to create services.js");
} else {
    const nodes = extract(sourceFile, [
        ["rewrite", SyntaxKind.FunctionDeclaration],
        ["defaultInstances", SyntaxKind.VariableStatement]]
    );

    await writeNodes(sourceFile, nodes, importShims, `${outputDir}/services.js`);

    const defaultInstancesNode = nodes.find(n => n.kind === SyntaxKind.VariableStatement);
    const defaultInstances = extractDefaultInstances(defaultInstancesNode);

    await Bun.write(`${outputDir}/defaultInstances.json`, JSON.stringify(defaultInstances));
}
