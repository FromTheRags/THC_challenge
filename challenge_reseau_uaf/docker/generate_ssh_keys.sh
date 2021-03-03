#!/bin/sh

rm -r private/keys/
rm -r ftp/keys/
mkdir private/keys/
mkdir ftp/keys/
ssh-keygen -b 2048 -t rsa -f private/keys/ouioui_ssh_key -q -N "" -C "ouioui@10.42.2.1"
mv private/keys/ouioui_ssh_key ftp/keys/ouioui_ssh_key
