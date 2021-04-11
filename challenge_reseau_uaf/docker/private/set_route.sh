#!/bin/sh

ip route del default
ip route add default via 10.42.3.2

