#!/bin/sh

sudo ip route del 10.43.2.0/24
sudo ip route del 10.43.3.0/24

sudo docker network rm external
sudo docker network rm dmz
sudo docker network rm internal

sudo docker rm firewall_container
sudo docker rm web_container
sudo docker rm private_container

