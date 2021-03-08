#!/bin/sh

ip route del default
ip route add default via 10.43.2.2

