#!/usr/bin/env python3

import platform
import subprocess

def fail(msg):
    with open('benkins-notification.txt', 'w') as f:
        f.write(msg)
    exit(1)

gradle = "./gradlew"
if platform.system == "Windows":
    gradle = "gradlew.bat"

try:
    result = subprocess.run([gradle, "build"], check=True)
    print(result)
except subprocess.CalledProcessError as e:
    fail("Failed to compile code. See the logs for more details.")

try:
    result = subprocess.run([gradle, "test"], check=True)
    print(result)
except subprocess.CalledProcessError as e:
    fail("Tests failed. See the logs for more details.")
