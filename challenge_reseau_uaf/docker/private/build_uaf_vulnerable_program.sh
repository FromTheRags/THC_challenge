#!/bin/sh

gcc -Wall -m32 -z execstack -fPIE -fstack-protector-all -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z,relro -o net_int_handler uaf_vulnerable_program.c
mv net_int_handler /home/ouioui/
chown root /home/ouioui/net_int_handler
chmod +s /home/ouioui/net_int_handler

