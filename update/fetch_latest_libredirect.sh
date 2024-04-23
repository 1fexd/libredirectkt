#!/usr/bin/env bash
wget https://raw.githubusercontent.com/libredirect/libredirect/master/src/config.json -O ../lib/src/main/resources/libredirect_config.json
wget https://raw.githubusercontent.com/libredirect/instances/main/data.json -O ../lib/src/main/resources/libredirect_instances.json
python3 write_metadata.py
cd ../libredirect/ || exit
./build.sh
