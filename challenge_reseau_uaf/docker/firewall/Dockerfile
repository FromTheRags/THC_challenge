# get alpine 3.13.2 32 bits with useful tools
FROM generic_alpine

# set password
RUN echo "root:afirewalldoesnotneedapasswordbutletspickoneanyway" | chpasswd

# cd /root
WORKDIR /root

# apply firewall rules for ip forwarding
COPY apply_rules.sh ./

# set default route to host
COPY set_route.sh ./

# set prompt
RUN echo "PS1='\u@firewall:\w\$ '" >> .bashrc

# stay alive (executed on startup)
ENTRYPOINT sleep infinity

