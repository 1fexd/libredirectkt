import { resolve } from "path";
import {
    createImport,
    extractNodes,
    extractDefaultInstances,
    toSourceFile,
    printNodes,
    getDefaultInstancesNode
} from "./extractor.js";
import { SyntaxKind } from "typescript";

function resolvePath(path: string): string {
    // @ts-expect-error
    return resolve(import.meta.dir, path);
}

async function main(inputFile: string, outputDir: string) {
    const sourceFile = toSourceFile(inputFile);

    if (!sourceFile) {
        throw new Error(`Services file does not exist at '${inputFile}'`);
    }

    const imports = [
        createImport("../shim/XMLHttpRequest.shim.ts", "XMLHttpRequest")
    ];
    const nodes = extractNodes(sourceFile, [
        ["rewrite", SyntaxKind.FunctionDeclaration],
        ["defaultInstances", SyntaxKind.VariableStatement]]
    );

    const servicesFile = Bun.file(resolve(outputDir, "services.js"));
    const writer = servicesFile.writer();
    printNodes(sourceFile, [...imports, ...nodes], (line) => writer.write(line + "\n"));
    writer.end();

    const defaultInstances = extractDefaultInstances(getDefaultInstancesNode(nodes));
    await Bun.write(resolve(outputDir, "defaultInstances.json"), JSON.stringify(defaultInstances));
}

const outputDir = resolvePath("src/generated");
const inputFile = resolvePath("../browser_extension/src/assets/javascripts/services.js");

// @ts-expect-error
await main(inputFile, outputDir);
