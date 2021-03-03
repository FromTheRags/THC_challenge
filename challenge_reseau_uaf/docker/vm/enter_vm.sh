#!/bin/sh

while ! ssh -p 2222 root@localhost
do
	echo "Trying again"
done
ssh -p 2222 root@localhost

