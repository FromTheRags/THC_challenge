#!/bin/sh

# ip addresses
NET_EXT=10.42.1.0/24
NET_DMZ=10.42.2.0/24
NET_INT=10.42.3.0/24
NET_ANY=10.42.0.0/16

# protocols ports
PT_FTP=21
PT_SSH=22
PT_TWO="21:22"		# ftp and ssh
PT_SRC="1024:65535"	# source ports range
PT_FRE=$PT_SRC		# free ports range
PT_ANY="0:65535"

# flush rules
iptables -t filter -F
iptables -t filter -X

# drop by default
iptables -t filter -P INPUT DROP
iptables -t filter -P OUTPUT DROP
iptables -t filter -P FORWARD DROP

# white list
# authorize established and related connections for all chains
iptables -t filter -A FORWARD -m state --state RELATED,ESTABLISHED -j ACCEPT
# from external to dmz : ftp and ssh
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_EXT --dport $PT_TWO -d $NET_DMZ -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_EXT --dport $PT_FRE -d $NET_DMZ -m state --state NEW -j ACCEPT
# from dmz to external : any port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_ANY -d $NET_EXT -m state --state NEW -j ACCEPT
# from dmz to internal : ssh
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_SSH -d $NET_INT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_FRE -d $NET_INT -m state --state NEW -j ACCEPT
# from internal to any : any port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_ANY -d $NET_ANY -m state --state NEW -j ACCEPT

