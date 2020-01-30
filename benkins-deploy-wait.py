#!/usr/bin/env python3

import netconsole
import sys

print("Waiting for robot to boot...")

def customPrintFn(s):
    sys.stdout.write(s.encode(sys.stdout.encoding, errors='replace').decode(sys.stdout.encoding))
    sys.stdout.write('\n')
    if "disabled and ready" in s:
        console.stop()
        exit(0)

console = netconsole.Netconsole(customPrintFn)
console.start("roborio-2175-frc.local")
