#!/bin/sh

sudo ip route del 10.42.2.0/24
sudo ip route del 10.42.3.0/24
sudo docker kill $(sudo docker ps --quiet)
sudo docker system prune --all --force

