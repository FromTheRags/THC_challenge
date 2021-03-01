#!/bin/sh

# preparation
sh generate_ssh_keys.sh

# build images
sudo docker build ./generic_alpine -t generic_alpine
sudo docker build ./firewall -t firewall
sudo docker build ./attack -t attack
sudo docker build ./ftp -t ftp
sudo docker build ./private -t private

# create networks
sudo docker network create --driver=bridge --subnet=192.168.1.0/24 --gateway=192.168.1.254 --opt com.docker.network.bridge.name=ext external
sudo docker network create --driver=bridge --subnet=192.168.2.0/24 --gateway=192.168.2.254 --opt com.docker.network.bridge.name=dmz dmz
sudo docker network create --driver=bridge --subnet=192.168.3.0/24 --gateway=192.168.3.254 --opt com.docker.network.bridge.name=int internal

# create firewall container
sudo docker create --name firewall_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network external --ip 192.168.1.2 firewall
#sudo docker network connect --ip 192.168.1.2 external firewall_container
sudo docker network connect --ip 192.168.2.2 dmz firewall_container
sudo docker network connect --ip 192.168.3.2 internal firewall_container

# create attack container (temporary)
sudo docker create --name attack_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network external --ip 192.168.1.1 attack
#sudo docker network connect --ip 192.168.1.1 external attack_container

# create ftp container
sudo docker create --name ftp_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network dmz --ip 192.168.2.1 ftp
#sudo docker network connect --ip 192.168.2.1 dmz ftp_container

# create private container
sudo docker create --name private_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network internal --ip 192.168.3.1 private
#sudo docker network connect --ip 192.168.3.1 internal private_container

