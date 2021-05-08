#!/bin/sh

sudo docker kill $(sudo docker ps --quiet)
sudo docker system prune --all --force

