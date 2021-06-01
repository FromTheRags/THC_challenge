#!/bin/sh

sudo docker network rm external_42
sudo docker network rm dmz_42
sudo docker network rm internal_42

sudo docker rm firewall_container_42
sudo docker rm attack_container_42
sudo docker rm ftp_container_42
sudo docker rm private_container_42

