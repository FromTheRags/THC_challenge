# get alpine 3.13.2 32 bits
FROM i386/alpine:3.13.2

# install useful tools (shell, edition, network, services, web, db, git)
RUN apk update && apk add bash vim acl wget curl netcat-openbsd nmap tcpdump iproute2 iptables lynx openrc git openssh apache2 apache2-utils mariadb mariadb-client php8 php8-apache2 php8-cli php8-phar php8-zlib php8-zip php8-bz2 php8-ctype php8-curl php8-pdo_mysql php8-mysqli php8-json php8-xml php8-dom php8-iconv php8-session php8-intl php8-gd php8-mbstring php8-opcache php8-tokenizer php8-simplexml

# add netcat alias
RUN echo "alias netcat=nc" >> /root/.bashrc

# set rc
RUN rc-status && touch /run/openrc/softlevel

