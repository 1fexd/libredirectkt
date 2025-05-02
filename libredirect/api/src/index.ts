import "core-js/stable/url/index"
import { rewrite } from "./generated/services"

function resolve(url: string, frontend: string, instance: string): string | undefined {
    const urlInstance = new URL(url);
    return rewrite(urlInstance, urlInstance, frontend, instance);
}

// @ts-expect-error
define(() => ({
    resolve: resolve
}))
