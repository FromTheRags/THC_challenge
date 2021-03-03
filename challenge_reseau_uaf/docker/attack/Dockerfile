FROM i386/debian:buster-slim

WORKDIR /root
RUN echo "PS1='${debian_chroot:+($debian_chroot)}\u@attack:\w\$ '" >> .bashrc

RUN apt-get update -y && \
apt-get install -yq curl vim telnet gnupg iputils-ping iproute2 nmap netcat net-tools wget && \
curl https://raw.githubusercontent.com/rapid7/metasploit-omnibus/master/config/templates/metasploit-framework-wrappers/msfupdate.erb > msfinstall && \
chmod 755 msfinstall && \
./msfinstall
COPY set_route.sh ./
RUN wget www.tux-planet.fr/public/hack/exploits/proftpd/proftpd-iac-remote-root-exploit.pl

ENTRYPOINT sleep infinity

