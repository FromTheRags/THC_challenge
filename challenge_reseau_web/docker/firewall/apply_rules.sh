#!/bin/sh

# ip addresses
NET_DMZ=10.43.2.0/24
NET_INT=10.43.3.0/24

# protocols ports
PT_FTP=21
PT_SSH=22
PT_WEB=80
PT_RG0="0:1023"		# ring 0 destination ports
PT_RG3="1024:65535"	# ring 3 destination ports
PT_SRC="1024:65535"	# source ports range
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
# from external to dmz : ssh and web
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC ! -s $NET_INT --dport $PT_SSH -d $NET_DMZ -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC ! -s $NET_INT --dport $PT_WEB -d $NET_DMZ -m state --state NEW -j ACCEPT
# from dmz to external : any root port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_RG0 ! -d $NET_INT -m state --state NEW -j ACCEPT
# from dmz to internal : ssh and web
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_SSH -d $NET_INT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_WEB -d $NET_INT -m state --state NEW -j ACCEPT
# from internal to any : any root port
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_RG0 -m state --state NEW -j ACCEPT

