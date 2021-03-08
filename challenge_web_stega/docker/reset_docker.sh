#!/bin/sh

sudo ip route del 10.43.2.0/24
sudo ip route del 10.43.3.0/24
sudo docker kill $(sudo docker ps --quiet)
sudo docker system prune --all --force

