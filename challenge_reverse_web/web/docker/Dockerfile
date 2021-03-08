FROM debian:stable-slim

RUN echo "root:goodserverpasswordtopreventanyattackbecauseitisverylong" | chpasswd
RUN apt-get update -y && apt-get install -yq apache2 mariadb-server php libapache2-mod-php php-mysql certbot python-certbot-apache
RUN sed -i '/<Directory \/var\/www\/>/,/<\/Directory>/ s/Options Indexes FollowSymLinks/Options -Indexes -FollowSymLinks/' /etc/apache2/apache2.conf
RUN sed -i '/<Directory \/var\/www\/>/,/<\/Directory>/ s/Require all granted/Require all granted\n\tSSLRequireSSL/' /etc/apache2/apache2.conf
RUN cp -a /var/lib/mysql /var/lib/mysql_bis
WORKDIR /var/www/
RUN rm -rf html/
COPY web/ ./
RUN chmod -R 755 html/
RUN sh config/config_mysql.sh
EXPOSE 80
EXPOSE 443
#RUN apache2ctl start && certbot --apache --non-interactive --agree-tos --register-unsafely-without-email --redirect --domain tryagain.dynamic-dns.net
ENTRYPOINT sh config/start_services.sh
