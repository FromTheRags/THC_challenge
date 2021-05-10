#!/bin/sh
#sudo apt update
#sudo apt install dos2unix
find . -type f -exec dos2unix "{}" \;
# git config --global core.autocrlf false
# Have you tried a .gitattributes file with:* text=auto
#Or fix by extension file (use only LF):*.svg text eol=lf

