import "core-js/stable/url/index"
import { rewrite } from "./generated/services"

function resolve(url: string, frontend: string, instance: string): string | undefined {
    const urlInstance = new URL(url);
    const type = "";
    return rewrite(urlInstance, null, frontend, instance, type);
}

define(() => ({
    resolve: resolve
}))
