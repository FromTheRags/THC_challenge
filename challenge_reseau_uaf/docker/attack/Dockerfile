# get alpine 3.13.2 32 bits
FROM generic_alpine

# set password
RUN echo "root:toto" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@attack:\w\$ '" >> .bashrc

# install ssh and configure root access
RUN apk update && apk add openssh && echo "PermitRootLogin yes" >> /etc/ssh/sshd_conf

# set default route
COPY set_route.sh ./

# set rc
RUN rc-status && touch /run/openrc/softlevel

# start ssh daemon (called at container startup)
ENTRYPOINT rc-service sshd stop; rc-service sshd start; sleep infinity

