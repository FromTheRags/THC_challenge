#!/bin/sh

# generate stega image
cd private/stega
sh stega.sh
cd ../..

# build images
sudo docker build ./generic_alpine -t generic_alpine
sudo docker build ./firewall -t firewall
sudo docker build ./web -t web
sudo docker build ./private -t private

# create networks
sudo docker network create --driver=bridge --subnet=10.43.1.0/24 --gateway=10.43.1.1 --opt com.docker.network.bridge.name=ext external
sudo docker network create --driver=bridge --subnet=10.43.2.0/24 --gateway=10.43.2.254 --opt com.docker.network.bridge.name=dmz dmz		# gateway to be deleted
sudo docker network create --driver=bridge --subnet=10.43.3.0/24 --gateway=10.43.3.254 --opt com.docker.network.bridge.name=int internal	# gateway to be deleted
sudo ip route del 10.43.2.0/24
sudo ip route del 10.43.3.0/24
sudo ip route add 10.43.2.0/24 via 10.43.1.2
sudo ip route add 10.43.3.0/24 via 10.43.1.2
sudo ip addr del 10.43.2.254/24 dev dmz	# delete gateway
sudo ip addr del 10.43.3.254/24 dev int	# delete gateway

# create firewall container
sudo docker create --name firewall_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network external --ip 10.43.1.2 firewall
sudo docker network connect --ip 10.43.2.2 dmz firewall_container
sudo docker network connect --ip 10.43.3.2 internal firewall_container

# create web container
sudo docker create --name web_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network dmz --ip 10.43.2.1 web

# create private container
sudo docker create --name private_container --cap-add=NET_ADMIN --cap-add=NET_RAW --network internal --ip 10.43.3.1 private

