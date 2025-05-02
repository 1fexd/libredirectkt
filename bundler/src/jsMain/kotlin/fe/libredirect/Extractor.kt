package fe.libredirect

import typescript.EmitHint
import typescript.ImportDeclaration
import typescript.ImportSpecifier
import typescript.ModifierLike
import typescript.NewLineKind
import typescript.Node
import typescript.PrinterOptions
import typescript.SourceFile
import typescript.Statement
import typescript.StringLiteral
import typescript.SyntaxKind
import typescript.VariableStatement
import typescript.asArray
import typescript.asSequence
import typescript.createPrinter
import typescript.createProgram
import typescript.factory
import typescript.forEachChild
import typescript.isArrayLiteralExpression
import typescript.isFunctionDeclaration
import typescript.isObjectLiteralExpression
import typescript.isPropertyAssignment
import typescript.isStringLiteral
import typescript.isVariableStatement
import typescript.iterator


fun updateNode(node: Node, requiredName: String, sourceFile: SourceFile): Statement? {
    return when {
        isFunctionDeclaration(node) -> {
            when {
                node.name?.text !== requiredName -> null
                else -> factory.updateFunctionDeclaration(
                    node,
                    arrayOf(factory.createToken(SyntaxKind.ExportKeyword).unsafeCast<ModifierLike>()),
                    node.asteriskToken, node.name, node.typeParameters?.asArray(),
                    node.parameters.asArray(), node.type, node.body
                )
            }
        }

        isVariableStatement(node) -> {
            when {
                node.declarationList.declarations.asArray()[0].name.getText(sourceFile) !== requiredName -> null
                else -> factory.updateVariableStatement(
                    node,
                    arrayOf(factory.createToken(SyntaxKind.ExportKeyword).unsafeCast<ModifierLike>()),
                    node.declarationList
                )
            }
        }

        else -> null
    }
}

fun extractNodes(sourceFile: SourceFile, extractNodes: Map<String, SyntaxKind>): List<Statement> {
    val nodes = mutableListOf<Statement>()
    forEachChild(sourceFile, cbNode = { node ->
        for ((name, kind) in extractNodes) {
            if (node.kind != kind) continue

            val newNode = updateNode(node, name, sourceFile)
            if (newNode == null) continue

            nodes.add(newNode)
        }

        false
    })

    return nodes
}

fun toSourceFile(file: String): SourceFile? {
    val program = createProgram(arrayOf(file), js("{ allowJs: true }"))
    return program.getSourceFileByPath(file)
}

fun getDefaultInstancesNode(nodes: Array<Node>): VariableStatement? {
    return nodes.firstOrNull { isVariableStatement(it) }?.unsafeCast<VariableStatement>()
}

fun extractDefaultInstances(node: Node?): MutableMap<String, List<String>> {
    val instances = mutableMapOf<String, List<String>>()
    if (node == null || !isVariableStatement(node)) return instances

    val declarations = node.declarationList.declarations
    val expression = declarations.asArray()[0].initializer

    if (expression == null || !isObjectLiteralExpression(expression)) return instances
    for (property in expression.properties.iterator()) {
        if (!isPropertyAssignment(property)) continue
        val name = property.name
        val initializer = property.initializer

        if (!isStringLiteral(name) || !isArrayLiteralExpression(initializer)) continue
        val texts = initializer.elements
            .asSequence()
            .filter(::isStringLiteral)
            .map { it.unsafeCast<StringLiteral>().text }
            .toList()

        instances[name.text] = texts
    }

    return instances
}

fun createImportSpecifier(identifier: String): ImportSpecifier {
    return factory.createImportSpecifier(false, undefined, factory.createIdentifier(identifier));
}

fun createImport(file: String, vararg identifiers: String): ImportDeclaration {
    val specifiers = identifiers.map { createImportSpecifier(it) }.toTypedArray()
    val namedImports = factory.createNamedImports(specifiers)

    val clause = factory.createImportClause(false, undefined, namedImports);
    return factory.createImportDeclaration(undefined, clause, factory.createStringLiteral(file));
}

fun printNodes(sourceFile: SourceFile, nodes: Array<Node>, emitLine: (line: String) -> Unit) {
    val printer = createPrinter(PrinterOptionsInit(newLine = NewLineKind.LineFeed))
    for (node in nodes) {
        val line = printer.printNode(EmitHint.Unspecified, node, sourceFile)
        emitLine(line)
    }
}


@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
@kotlin.internal.InlineOnly
public inline fun PrinterOptionsInit(
    removeComments: Boolean? = false,
    newLine: NewLineKind? = undefined,
    omitTrailingSemicolon: Boolean? = false,
    noEmitHelpers: Boolean? = undefined,
): PrinterOptions {
    val o = js("({})")
    o["removeComments"] = removeComments
    o["newLine"] = newLine
    o["omitTrailingSemicolon"] = omitTrailingSemicolon
    o["noEmitHelpers"] = noEmitHelpers
    return o
}
