#!/bin/sh

sudo ip route del 10.42.2.0/24
sudo ip route del 10.42.3.0/24

sudo docker network rm external
sudo docker network rm dmz
sudo docker network rm internal

sudo docker rm firewall_container
sudo docker rm ftp_container
sudo docker rm private_container

