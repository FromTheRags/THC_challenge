#!/bin/sh

# preparation
sh generate_ssh_keys.sh

# build images
sudo docker build ./generic_alpine -t generic_alpine_42
sudo docker build ./attack -t attack_42
sudo docker build ./firewall -t firewall_42
sudo docker build ./ftp -t ftp_42
sudo docker build ./private -t private_42

# create networks
sudo docker network create --driver=bridge --subnet=10.42.1.0/24 --gateway=10.42.1.254 --opt com.docker.network.bridge.name=ext_42 external_42	# gateway to be deleted
sudo docker network create --driver=bridge --subnet=10.42.2.0/24 --gateway=10.42.2.254 --opt com.docker.network.bridge.name=dmz_42 dmz_42	# gateway to be deleted
sudo docker network create --driver=bridge --subnet=10.42.3.0/24 --gateway=10.42.3.254 --opt com.docker.network.bridge.name=int_42 internal_42	# gateway to be deleted
sudo ip addr del 10.42.1.254/24 dev ext_42	# delete gateway
sudo ip addr del 10.42.2.254/24 dev dmz_42	# delete gateway
sudo ip addr del 10.42.3.254/24 dev int_42	# delete gateway

# create firewall container
sudo docker create --name firewall_container_42 --cap-add=NET_ADMIN --cap-add=NET_RAW --network external_42 --ip 10.42.1.2 firewall_42
sudo docker network connect --ip 10.42.2.2 dmz_42 firewall_container_42
sudo docker network connect --ip 10.42.3.2 internal_42 firewall_container_42

# create attack container
sudo docker create --name attack_container_42 --cap-add=NET_ADMIN --cap-add=NET_RAW --cap-add=NET_BIND_SERVICE --network external_42 --ip 10.42.1.1 attack_42

# create ftp container
sudo docker create --name ftp_container_42 --cap-add=NET_ADMIN --cap-add=NET_RAW --network dmz_42 --ip 10.42.2.1 ftp_42

# create private container
sudo docker create --name private_container_42 --cap-add=NET_ADMIN --cap-add=NET_RAW --network internal_42 --ip 10.42.3.1 private_42

