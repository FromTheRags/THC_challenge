#!/bin/sh

sudo docker stop attack_container_43
sudo docker stop firewall_container_43
sudo docker stop web_container_43
sudo docker stop private_container_43
#sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

