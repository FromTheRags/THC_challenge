# get alpine 3.13.2 32 bits
FROM generic_alpine

# set password
RUN echo "root:agoodpasswordforavsftpdserverversion3topreventanyattackbecause" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@ftp:\w\$ '" >> .bashrc

# install tools and vulnerable ftp server
RUN apk update && apk add openssh git build-base linux-pam-dev openssh linux-headers libc6-compat

# install vulnerable ftp daemon
COPY install_vulnerable_ftpd.sh ./
RUN sh install_vulnerable_ftpd.sh && rm -f install_vulnerable_ftpd.sh

# set default route
COPY set_route.sh ./

# add users
COPY add_users_ftp.sh ./
RUN sh add_users_ftp.sh && rm -f add_users_ftp.sh

# set ssh
COPY keys/ouioui_ssh_key ./
RUN mkdir /home/ouioui/.ssh; mv ouioui_ssh_key /home/ouioui/.ssh/id_rsa && chown -R ouioui /home/ouioui/.ssh && chmod -R 700 /home/ouioui/.ssh/

# set rc
RUN rc-status && touch /run/openrc/softlevel

# start ftp and ssh daemons (called at container startup)
ENTRYPOINT (vsftpd &); rc-service sshd stop; rc-service sshd start; sleep infinity

