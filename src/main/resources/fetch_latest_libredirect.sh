#!/usr/bin/env bash
wget https://raw.githubusercontent.com/libredirect/libredirect/master/src/config.json -O libredirect_config.json
wget https://raw.githubusercontent.com/libredirect/instances/main/data.json -O libredirect_instances.json
python3 write_metadata.py
