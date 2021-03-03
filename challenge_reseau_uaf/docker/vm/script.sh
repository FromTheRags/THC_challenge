#!/bin/sh

sed -i -e "s/ftp/archive/g" /etc/apt/sources.list
apt-get update -y
apt-get install -yq --force-yes wget debconf-utils ssh vim telnet net-tools
echo "proftpd-basic shared/proftpd/inetd_or_standalone select standalone" | debconf-set-selections
wget http://launchpadlibrarian.net/58184985/proftpd-basic_1.3.3a-4_i386.deb
apt-get -fyq install --force-yes libcap2 ucf update-inetd libfile-temp-perl libfile-copy-recursive-perl perl libdb4.7 openbsd-inetd
echo '10.0.2.15 debian-i386.__' >> /etc/hosts
dpkg -i proftpd-basic_1.3.3a-4_i386.deb
rm -f proftpd-basic_1.3.3a-4_i386.deb

