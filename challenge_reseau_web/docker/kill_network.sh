#!/bin/sh

sudo docker kill firewall_container
sudo docker kill web_container
sudo docker kill private_container
sudo sysctl -w $(cat .net_conf_save | sed 's/ //g')

