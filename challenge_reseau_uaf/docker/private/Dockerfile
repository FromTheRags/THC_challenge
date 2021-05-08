# get alpine 3.13.2 32 bits with useful tools
FROM generic_alpine

# set password
RUN echo "root:agoodpasswordforaprivateservertopreventanattack!!!" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='\u@private:\w\$ '" >> .bashrc

# install necessary tools
#RUN apk update && apk add openssh-server musl-dev build-base python3 python3-dev py3-pip libffi-dev rust cargo libressl-dev
RUN apk update && apk add openssh-server musl-dev build-base python3 python3-dev py3-pip libffi-dev libressl-dev

# install pwn python library
RUN export CRYPTOGRAPHY_DONT_BUILD_RUST=1 && python3 -m pip install --use-deprecated=legacy-resolver pwn

# install ssh client
RUN apk add openssh

# set default route
COPY set_route.sh ./

# add users
COPY add_users_private.sh ./
RUN sh add_users_private.sh && rm -f add_users_private.sh

# set ssh to allow ouioui from ftp
COPY keys/ouioui_ssh_key.pub ./
RUN mkdir /home/ouioui/.ssh; cat ouioui_ssh_key.pub >> /home/ouioui/.ssh/authorized_keys; rm -f ouioui_ssh_key.pub; chown -R ouioui /home/ouioui/.ssh && chmod -R 700 /home/ouioui/.ssh
RUN rc-status && touch /run/openrc/softlevel

# install uaf vulnerable program
COPY uaf_vulnerable_program.c ./
COPY build_uaf_vulnerable_program.sh ./
RUN sh build_uaf_vulnerable_program.sh; rm -f uaf_vulnerable_program.c; rm -f build_uaf_vulnerable_program.sh

# copy flag
COPY flag.txt ./
RUN chown root flag.txt; chmod 700 flag.txt

# start sshd (executed on startup)
ENTRYPOINT rc-service sshd stop; rc-service sshd start; sleep infinity

