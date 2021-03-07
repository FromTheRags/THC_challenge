#!/bin/sh

sudo docker stop firewall_container
sudo docker stop web_container
sudo docker stop private_container
sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

