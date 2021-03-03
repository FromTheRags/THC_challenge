#!/bin/sh

sudo ip route del 192.168.2.0/24
sudo ip route del 192.168.3.0/24

sudo docker network rm external
sudo docker network rm dmz
sudo docker network rm internal

sudo docker rm firewall_container
sudo docker rm ftp_container
sudo docker rm private_container

