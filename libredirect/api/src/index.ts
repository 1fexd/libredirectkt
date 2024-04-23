import "core-js/stable/url/index"
import { rewrite } from "./generated/services"

// @ts-ignore
define(() => ({
    resolve: (url: string, frontend: string, instance: string): string | undefined => rewrite(new URL(url), frontend, instance)
}))
