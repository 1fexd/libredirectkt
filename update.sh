#!/usr/bin/env bash
pushd update || exit
./fetch_latest_libredirect.sh
popd || exit
./gradlew -p bundler jvmRun --args="$(pwd)"
