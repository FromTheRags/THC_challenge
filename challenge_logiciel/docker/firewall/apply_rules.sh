#!/bin/sh

# ip addresses
NET_EXT=192.168.1.0/24
NET_DMZ=192.168.2.0/24
NET_INT=192.168.3.0/24
NET_ANY=any

# protocols ports
PT_FTP=21
PT_SSH=22
PT_TWO="21:22"		# SSH + FTP
PT_SRC="1024:65535"	# source range
PT_ANY="0:65535"

# flush rules
iptables -t filter -F
iptables -t filter -X

# drop by default
iptables -t filter -P INPUT DROP
iptables -t filter -P OUTPUT DROP
iptables -t filter -P FORWARD DROP

# white list
iptables -t filter -A FORWARD -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_EXT --dport $PT_TWO -d $NET_DMZ -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_ANY -d $NET_EXT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_SSH -d $NET_INT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_ANY -d $NET_DMZ -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_ANY -d $NET_EXT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p icmp -j ACCEPT

