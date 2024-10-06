#!/usr/bin/env bash
wget https://raw.githubusercontent.com/libredirect/libredirect/master/src/config.json -O ../lib/src/main/resources/libredirect_config.json
wget https://raw.githubusercontent.com/libredirect/instances/main/data.json -O ../lib/src/main/resources/libredirect_instances.json
.venv/bin/python write_metadata.py
