#!/bin/sh

echo "Welcome to the Dockerfile generation program\n"
echo "Please, select the debian version you want to use to host the vulnerable ftp server :"
echo "\t0 : debian 10 - 32 bits"
echo "\t1 : debian 10 - 64 bits"
echo "\t2 : debian  6 - 32 bits"
echo "\t3 : debian  6 - 64 bits"

FILE=""
if test -n $1; then
	$FILE=$1
else
	$FILE=Dockerfile
fi;
$FILE=Dockerfile_test	# TO REMOVE

VERSION=""
read $VERSION

case $VERSION in
	0) echo "# base image (debian 10 32bits)\nFROM i386/debian:buster-slim" > $FILE;;
	1) echo "# base image (debian 10 64bits)\nFROM debian:buster-slim" > $FILE;;
	2) echo "# base image (debian 6 32bits)\nFROM lpenz/debian-squeeze-i386" > $FILE;;
	3) echo "# base image (debian 6 64bits)\nROM lpenz/debian-squeeze-amd64" > $FILE;;
	*) echo "Incorrect entry. Leaving"; exit;;
esac

echo 
echo "RUN echo 'root:agoodpasswordforaproftpserverversion3topreventanyattackbecause' | chpasswd"

echo 
	

