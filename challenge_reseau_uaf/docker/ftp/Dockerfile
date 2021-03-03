# get debian 10 slim 64 bits
FROM debian:buster-slim

# set password
RUN echo "root:agoodpasswordforaproftpserverversion3topreventanyattackbecause" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@ftp:\w\$ '" >> .bashrc

# install tools and vulnerable ftp server
RUN apt-get update -y && \
apt-get install -yq wget debconf-utils ssh curl vim telnet iputils-ping nmap netcat git build-essential libpam0g-dev iproute2

# install vulnerable ftp daemon
COPY install_vulnerable_ftpd.sh ./
RUN sh install_vulnerable_ftpd.sh && rm -f install_vulnerable_ftpd.sh

# copy script to set default route (called at container startup)
COPY set_route.sh ./

# add users
COPY add_users_ftp.sh ./
RUN sh add_users_ftp.sh && rm -f add_users_ftp.sh

# set ssh
COPY keys/ouioui_ssh_key ./
RUN mkdir /home/ouioui/.ssh; mv ouioui_ssh_key /home/ouioui/.ssh/id_rsa && chown -R ouioui /home/ouioui/.ssh && chmod -R 700 /home/ouioui/.ssh/

# start ftp and ssh daemons (called at container startup)
ENTRYPOINT (vsftpd &); service start ssh; sleep infinity
#libc6-compat

