#!/bin/sh

sudo docker kill attack_container_43
sudo docker kill firewall_container_43
sudo docker kill web_container_43
sudo docker kill private_container_43
#sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

