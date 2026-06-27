#!/usr/bin/env bash
# Update config, instances and build metadata via Gradle tasks in buildSrc
./gradlew -p lib updateAll
# Build code extractor
./gradlew -p bundler jsProductionExecutableCompileSync
# Run bundler, extracts code, builds extracted code with stubs, compiles zipline
./gradlew -p bundler jvmRun --args="$(pwd)"
