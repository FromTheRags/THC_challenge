#!/bin/sh

# debian 10
ip route del default
ip route add default via 192.168.2.2

# debian 6
#route del default
#route add default gw 192.168.2.2
