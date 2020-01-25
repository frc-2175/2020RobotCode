#!/usr/bin/env python3

import os
import platform
import subprocess

def fail(msg):
    print(msg)
    with open('benkins-notification.txt', 'w') as f:
        f.write(msg)
    exit(1)

print(os.getcwd())

gradle = "./gradlew"
if platform.system == "Windows":
    gradle = "gradlew.bat"

try:
    subprocess.run([gradle, "build"], check=True)
except subprocess.CalledProcessError as e:
    fail("Failed to compile code. See the logs for more details.")

try:
    subprocess.run([gradle, "test"], check=True)
except subprocess.CalledProcessError as e:
    fail("Tests failed. See the logs for more details.")
