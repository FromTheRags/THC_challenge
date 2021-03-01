#!/bin/sh

sudo docker network rm external
sudo docker network rm dmz
sudo docker network rm internal

sudo docker rm firewall_container
sudo docker rm attack_container
sudo docker rm ftp_container
sudo docker rm private_container

