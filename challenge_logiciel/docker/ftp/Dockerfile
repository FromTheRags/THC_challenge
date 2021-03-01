FROM i386/debian:buster-slim

RUN echo "root:agoodpasswordforaproftpserverversion3topreventanyattackbecause" | chpasswd

WORKDIR /root
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@ftp:\w\$ '" >> .bashrc
RUN apt-get update -y && \
apt-get install -yq wget debconf-utils ssh curl vim telnet iputils-ping iproute2 nmap netcat && \
echo "proftpd-basic shared/proftpd/inetd_or_standalone select standalone" | debconf-set-selections && \
wget http://launchpadlibrarian.net/106035913/libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb && \
wget http://launchpadlibrarian.net/106035917/openssl_0.9.8o-5ubuntu1.7_i386.deb && \
wget http://launchpadlibrarian.net/58184985/proftpd-basic_1.3.3a-4_i386.deb && \
apt-get --fix-broken install -yq --allow-downgrades lsb-base ./libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb ./openssl_0.9.8o-5ubuntu1.7_i386.deb ./proftpd-basic_1.3.3a-4_i386.deb && \
rm -f libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb && \
rm -f openssl_0.9.8o-5ubuntu1.7_i386.deb && \
rm -f proftpd-basic_1.3.3a-4_i386.deb
COPY set_route.sh ./
COPY add_users_ftp.sh ./
RUN sh add_users_ftp.sh && rm -f add_users_ftp.sh
COPY keys/ouioui_ssh_key ./
RUN mkdir /home/ouioui/.ssh; mv ouioui_ssh_key /home/ouioui/.ssh/id_rsa && chown -R ouioui /home/ouioui/.ssh && chmod -R 700 /home/ouioui/.ssh/
#EXPOSE 20
#EXPOSE 21
#EXPOSE 22

ENTRYPOINT service proftpd start && service ssh start && sleep infinity

