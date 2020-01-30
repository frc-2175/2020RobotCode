#!/usr/bin/env python3

import netconsole

def customPrintFn(s):
    netconsole._output_fn(s)
    if "disabled and ready" in s:
        exit(0)

console = netconsole.Netconsole(customPrintFn)
console.start("roborio-2175-frc.local")
