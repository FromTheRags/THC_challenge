#!/bin/sh

sudo docker network rm external_43
sudo docker network rm dmz_43
sudo docker network rm internal_43

sudo docker rm firewall_container_43
sudo docker rm attack_container_43
sudo docker rm web_container_43
sudo docker rm private_container_43

