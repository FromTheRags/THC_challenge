#!/bin/sh

# ip addresses
NET_EXT=10.42.1.0/24
NET_DMZ=10.43.2.0/24
NET_INT=10.43.3.0/24
NET_ANY=10.43.0.0/16

# protocols ports
PT_SSH=22
PT_WEB=80
PT_RES="0:1023"		# reserved destination ports
PT_SRC="1024:65535"	# source ports range

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
# from external to dmz : ssh and web
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_EXT --dport $PT_SSH -d $NET_DMZ -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_EXT --dport $PT_WEB -d $NET_DMZ -m state --state NEW -j ACCEPT
# from dmz to external : any reserved port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_RES -d $NET_EXT -m state --state NEW -j ACCEPT
# from dmz to internal : ssh and web
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_SSH -d $NET_INT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_WEB -d $NET_INT -m state --state NEW -j ACCEPT
# from internal to any : any reserved port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_RES -d $NET_ANY -m state --state NEW -j ACCEPT

