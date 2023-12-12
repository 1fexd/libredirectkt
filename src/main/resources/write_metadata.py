import os
import sys
import time
from datetime import datetime

from fwutil.FileWriter import open_file

now = int(time.time() * 1000.0)

cwd = os.getcwd()
clearurlkt_dir = os.path.abspath(os.path.join(cwd, os.pardir))
source_path = os.path.join(clearurlkt_dir, "kotlin")

with open_file(os.path.join(source_path, "LibRedirectMetadata.kt")) as fw:
    fw.write_multiline(f"""
        object LibRedirectMetadata {{
            const val fetchedAt = {now}L
        }}
    """)
