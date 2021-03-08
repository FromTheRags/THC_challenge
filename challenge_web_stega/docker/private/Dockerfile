# get alpine 3.13.2 32 bits with useful tools
FROM generic_alpine

# set password
RUN echo "root:Skywalker42!" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='\u@private:\w\$ '" >> .bashrc

# add test page
RUN echo "<html><body><h1>It works too!</h1></body></html>" > /var/www/localhost/htdocs/index.html

# add stega image
COPY stega/magical.jpg .
COPY stega/chess.jpg .
COPY stega/starwars.txt .
RUN zip starwars.zip starwars.txt; \
cat chess.jpg starwars.zip > cat.jpg; \
steghide embed -cf magical.jpg -ef cat.jpg -sf it_s_magical.jpg -p "magical"; \
rm -f magical.jpg; \
rm -f chess.jpg; \
rm -f cat.jpg; \
rm -f starwars.txt; \
mv it_s_magical.jpg /var/www/localhost/htdocs

# configure ssh to allow root login
RUN echo "PermitRootLogin yes" >> /etc/ssh/sshd_config

# set default route
COPY set_route.sh ./

# start apache and sshd (executed at container startup)
ENTRYPOINT rc-service apache2 stop; rc-service apache2 start; rc-service sshd stop; rc-service sshd start; sleep infinity

