#!/usr/bin/env python3

import platform
import subprocess

def fail(msg):
    print(msg)
    with open('benkins-notification.txt', 'w') as f:
        f.write(msg)
    exit(1)

gradle = "./gradlew"
if platform.system() == "Windows":
    gradle = "gradlew.bat"

try:
    subprocess.run(["pip3", "install", "-r", "requirements.txt"])
except subprocess.CalledProcessError as e:
    fail("Failed to install Python dependencies.")

try:
    subprocess.run([gradle, "build"], check=True)
except subprocess.CalledProcessError as e:
    fail("Failed to compile code. See the logs for more details.")

try:
    subprocess.run([gradle, "test"], check=True)
except subprocess.CalledProcessError as e:
    fail("Tests failed. See the logs for more details.")

try:
    subprocess.run([gradle, "deploy"], check=True)
except subprocess.CalledProcessError as e:
    fail("Failed to deploy code to the roboRIO. See the logs for more details.")

try:
    subprocess.run(["python3", "benkins-deploy-wait.py"], timeout=30, check=True)
except:
    fail("Robot did not boot successfully. See the logs for more details.")
