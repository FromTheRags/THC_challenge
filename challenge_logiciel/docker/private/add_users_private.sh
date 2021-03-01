#!/bin/sh

create_user ()
{
	USER=$1
	PASSWORD=$2
	adduser $USER -s /bin/bash -D
	echo "${USER}:${PASSWORD}" | chpasswd
}

create_user martine jemappellemartineetjhabitealaplage
create_user ouioui jemappelleouiouietjaiunbeautaxi
create_user garfield miaoumiiiiaaaoumiaooouuumiaouu

