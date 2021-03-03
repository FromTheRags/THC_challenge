#!/bin/sh

FILE=debian_squeeze_i386_standard.qcow2

if ! test -f $FILE; then
	wget https://people.debian.org/~aurel32/qemu/i386/$FILE
fi

qemu-system-i386 -m 512 -nic user,model=virtio,hostfwd=tcp::2222-:22,hostfwd=tcp::2121-:21,hostfwd=tcp::2020-:20 -drive file=$FILE -display none -daemonize -monitor unix:/tmp/qemu.sock,server,nowait

