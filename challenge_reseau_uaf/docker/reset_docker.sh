#!/bin/sh

sudo ip route del 192.168.2.0/24
sudo ip route del 192.168.3.0/24
sudo docker kill $(sudo docker ps --quiet)
sudo docker system prune --all --force

