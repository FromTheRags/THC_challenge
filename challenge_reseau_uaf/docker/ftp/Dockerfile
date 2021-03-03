# uncomment the debian version you want to use and uncomment the matching installation method
#FROM i386/debian:buster-slim        # debian 10 - 64bits
FROM debian:buster-slim
#FROM lpenz/debian-squeeze-i386      # debian  6 - 32 bits
#FROM lpenz/debian-squeeze-amd64     # debian  6 - 64 bits 
# debian 6.0.10 slim 32bits
#FROM debian/eol@sha256:844f678dc4c39023d5b1ac5b62190b7012d725249d8e9fb13c48d3cd6004ede9
# debian 8.11 slim 32bits
#FROM i386/debian:8.11-slim
# ubntu 10.04.4 32bits
#FROM yamamuteki/ubuntu-lucid-i386
#FROM generic_alpine

# set password
RUN echo "root:agoodpasswordforaproftpserverversion3topreventanyattackbecause" | chpasswd

#RUN apk update && apk add g++ make git linux-pam-dev linux-headers

# cd /root
WORKDIR /root

#RUN git clone https://github.com/Elizafox/libutx.git
#RUN apk del musl
#RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
#wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.33-r0/glibc-2.33-r0.apk && \
#apk add glibc-2.33-r0.apk

# set prompt
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@ftp:\w\$ '" >> .bashrc

# install tools and vulnerable ftp server
RUN apt-get update -y && \
apt-get install -yq wget debconf-utils ssh curl vim telnet iputils-ping nmap netcat git build-essential libpam0g-dev iproute2

#apt-get install -yq wget debconf-utils ssh curl vim telnet iputils-ping iproute2 nmap netcat git && \
#echo "proftpd-basic shared/proftpd/inetd_or_standalone select standalone" | debconf-set-selections && \
#wget http://launchpadlibrarian.net/106035913/libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb && \
#wget http://launchpadlibrarian.net/106035917/openssl_0.9.8o-5ubuntu1.7_i386.deb && \
#wget http://launchpadlibrarian.net/58184985/proftpd-basic_1.3.3a-4_i386.deb && \
#apt-get --fix-broken install -yq --allow-downgrades lsb-base ./libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb ./openssl_0.9.8o-5ubuntu1.7_i386.deb ./proftpd-basic_1.3.3a-4_i386.deb && \
#rm -f libssl0.9.8_0.9.8o-5ubuntu1.7_i386.deb && \
#rm -f openssl_0.9.8o-5ubuntu1.7_i386.deb && \
#rm -f proftpd-basic_1.3.3a-4_i386.deb

#RUN sed -i -e "s/archive/old-releases/g" /etc/apt/sources.list
#COPY proftpd-basic_1.3.3a-4_i386.deb ./
#RUN apt-get update -y && \
#apt-get install -yq --force-yes wget debconf-utils ssh vim telnet net-tools && \
#echo "proftpd-basic shared/proftpd/inetd_or_standalone select standalone" | debconf-set-selections && \
#apt-get -fyq install --force-yes libcap2 ucf update-inetd libfile-temp-perl libfile-copy-recursive-perl perl libdb4.7 && \
#apt-get -fyq install --force-yes libcap2 ucf update-inetd libfile-temp-perl libfile-copy-recursive-perl perl libdb4.7 openbsd-inetd && \
#dpkg -i proftpd-basic_1.3.3a-4_i386.deb && \
#rm -f proftpd-basic_1.3.3a-4_i386.deb
#wget http://launchpadlibrarian.net/37083117/proftpd-basic_1.3.2c-1_i386.deb && \
#dpkg -i proftpd-basic_1.3.2c-1_i386.deb && \
#rm -f proftpd-basic_1.3.2c-1_i386.deb

#RUN git clone https://github.com/lyndon160/vsftpd-backdoor.git
#RUN apt-get install -yq --force-yes lftp
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

#EXPOSE 20
#EXPOSE 21
#EXPOSE 22

# start ftp and ssh daemons (called at container startup)
#ENTRYPOINT service proftpd start && service ssh start && sleep infinity
ENTRYPOINT (vsftpd &); service start ssh; sleep infinity
#ENTRYPOINT sleep infinity

