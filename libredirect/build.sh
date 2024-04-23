#!/usr/bin/env bash

cd api || exit
bun run extractLibRedirect.ts
bun build src/index.ts --outdir dist --minify
cd ..
