import os
import sys
import time
from datetime import datetime

from fwutil.FileWriter import open_file

now = int(time.time() * 1000.0)

cwd = os.getcwd()
libredirectkt = os.path.abspath(os.path.join(cwd, os.pardir))
source_path = os.path.join(libredirectkt, "lib", "src", "main", "kotlin")

with open_file(os.path.join(source_path, "LibRedirectMetadata.kt")) as fw:
    fw.write_multiline(f"""
        public object LibRedirectMetadata {{
            public const val fetchedAt: Long = {now}L
        }}
    """)
