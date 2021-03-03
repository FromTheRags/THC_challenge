#!/bin/sh
#gcc -fno-stack-protector -z execstack -no-pie -m32 code.c -o uaf
#gcc -fstack-protector-all -Wall -z execstack code.c -fPIE -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z ,relro -o uaf
gcc -Wall -z execstack -fPIE -fstack-protector-all -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z,relro -o uaf code.c
sudo chown root uaf
sudo chmod +s uaf
python3 soluce.py

