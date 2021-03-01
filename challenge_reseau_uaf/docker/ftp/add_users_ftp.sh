#!/bin/sh

create_user ()
{
	USER=$1
	PASSWORD=$2
	adduser $USER --shell /bin/bash --disabled-password
	echo "${USER}:${PASSWORD}" | chpasswd
}

create_user martine jemappelleraimartineetjhabiteraialaplage
create_user ouioui jemappelaisouiouietjavaisunbeautaxi
create_user garfield miaou!!!miiiiaaaou!miaooouuu!!miaouu!
create_user john jamesjackjonathanjimjasonjson

