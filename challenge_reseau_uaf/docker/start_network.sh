#!/bin/sh

#if ! test -f .net_conf_save; then
#	sysctl net.ipv4.ip_forward > .net_conf_save
#fi
sudo sysctl -w net.ipv4.ip_forward=1
sudo docker start firewall_container_42
sudo docker exec firewall_container_42 /root/apply_rules.sh
sudo docker start attack_container_42
sudo docker exec attack_container_42 /root/set_route.sh
sudo docker start ftp_container_42
sudo docker exec ftp_container_42 /root/set_route.sh
sudo docker start private_container_42
sudo docker exec private_container_42 /root/set_route.sh

