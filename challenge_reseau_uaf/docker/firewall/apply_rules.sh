#!/bin/sh

# ip addresses
NET_DMZ=192.168.2.0/24
NET_INT=192.168.3.0/24

# protocols ports
PT_FTP=21
PT_SSH=22
PT_TWO="21:22"		# ftp and ssh
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
# authorize established and related connections
iptables -t filter -A FORWARD -m state --state RELATED,ESTABLISHED -j ACCEPT
# from external to dmz : ftp and ssh
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC ! -s $NET_INT --dport $PT_TWO -d $NET_DMZ -m state --state NEW -j ACCEPT
# from dmz to external : any root port
#iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_RG0 ! -d $NET_INT -m state --state NEW -j ACCEPT
iptables -t filter -A FORWARD -p tcp --sport $PT_SRC -s $NET_DMZ --dport $PT_RG0 ! -d $NET_INT -j ACCEPT
iptables -t filter -A FORWARD -p tcp --dport $PT_SRC -d $NET_DMZ --sport $PT_RG0 ! -s $NET_INT -j ACCEPT
# from dmz to internal : ssh
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_DMZ --dport $PT_SSH -d $NET_INT -m state --state NEW -j ACCEPT
# from internal to any : any
iptables -t filter -A FORWARD -p tcp --syn --sport $PT_SRC -s $NET_INT --dport $PT_RG0 -m state --state NEW -j ACCEPT
# allow icmp
#iptables -t filter -A FORWARD -p icmp -j ACCEPT

