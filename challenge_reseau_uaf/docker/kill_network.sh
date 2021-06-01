#!/bin/sh

sudo docker kill attack_container_42
sudo docker kill firewall_container_42
sudo docker kill ftp_container_42
sudo docker kill private_container_42
#sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

