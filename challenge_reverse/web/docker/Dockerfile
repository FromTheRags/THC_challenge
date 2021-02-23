FROM debian:stable-slim

RUN echo "root:goodserverpasswordtopreventanyattackbecauseitisverylong" | chpasswd
RUN apt-get update -y && apt-get install -y -q apache2 mariadb-server php libapache2-mod-php php-mysql
RUN sed -i '/<Directory \/var\/www\/>/,/<\/Directory>/ s/Options Indexes FollowSymLinks/Options -Indexes -FollowSymLinks/' /etc/apache2/apache2.conf
RUN cp -a /var/lib/mysql /var/lib/mysql_bis
WORKDIR /var/www/
RUN rm -rf html/
COPY web/ ./
RUN chmod -R 755 html/
EXPOSE 80
ENTRYPOINT sh config/start_services.sh
