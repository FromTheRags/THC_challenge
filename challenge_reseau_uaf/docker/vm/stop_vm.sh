#!/bin/sh

echo system_powerdown | socat - unix-connect:/tmp/qemu.sock

