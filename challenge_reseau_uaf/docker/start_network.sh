#!/bin/sh

if ! test -f .net_conf_save; then
	sysctl net.ipv4.ip_forward > .net_conf_save
fi
sudo sysctl -w net.ipv4.ip_forward=1
sudo docker start firewall_container
sudo docker exec firewall_container /root/apply_rules.sh
sudo docker start attack_container
sudo docker exec attack_container /root/set_route.sh
sudo docker start ftp_container
sudo docker exec ftp_container /root/set_route.sh
sudo docker start private_container
sudo docker exec private_container /root/set_route.sh

