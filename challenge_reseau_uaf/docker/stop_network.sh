#!/bin/sh

sudo docker stop attack_container_42
sudo docker stop firewall_container_42
sudo docker stop ftp_container_42
sudo docker stop private_container_42
#sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

