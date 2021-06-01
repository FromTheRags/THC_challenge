#!/bin/sh

# generate stega image
cd private/stega
sh stega.sh
cd ../..

# build images
sudo docker build ./generic_alpine -t generic_alpine_43
sudo docker build ./attack -t attack_43
sudo docker build ./firewall -t firewall_43
sudo docker build ./web -t web_43
sudo docker build ./private -t private_43

# create networks
sudo docker network create --driver=bridge --subnet=10.43.1.0/24 --gateway=10.43.1.254 --opt com.docker.network.bridge.name=ext_43 external_43	# gateway to be deleted
sudo docker network create --driver=bridge --subnet=10.43.2.0/24 --gateway=10.43.2.254 --opt com.docker.network.bridge.name=dmz_43 dmz_43	# gateway to be deleted
sudo docker network create --driver=bridge --subnet=10.43.3.0/24 --gateway=10.43.3.254 --opt com.docker.network.bridge.name=int_43 internal_43	# gateway to be deleted
sudo ip addr del 10.43.1.254/24 dev ext_43	# delete gateway
sudo ip addr del 10.43.2.254/24 dev dmz_43	# delete gateway
sudo ip addr del 10.43.3.254/24 dev int_43	# delete gateway

# create firewall container
sudo docker create --name firewall_container_43 --cap-add=NET_ADMIN --cap-add=NET_RAW --network external_43 --ip 10.43.1.2 firewall_43
sudo docker network connect --ip 10.43.2.2 dmz_43 firewall_container_43
sudo docker network connect --ip 10.43.3.2 internal_43 firewall_container_43

# create attack container
sudo docker create --name attack_container_43 --cap-add=NET_ADMIN --cap-add=NET_RAW --network external_43 --ip 10.43.1.1 attack_43

# create web container
sudo docker create --name web_container_43 --cap-add=NET_ADMIN --cap-add=NET_RAW --network dmz_43 --ip 10.43.2.1 web_43

# create private container
sudo docker create --name private_container_43 --cap-add=NET_ADMIN --cap-add=NET_RAW --network internal_43 --ip 10.43.3.1 private_43

