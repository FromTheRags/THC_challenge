# get alpine 3.13.2 32 bits with useful tools
FROM generic_alpine

# set password
RUN echo "root:aterriblepasswordbutnotthatmuchcauseitisquitelong" | chpasswd

# cd /root
WORKDIR /root

# set prompt
RUN echo "PS1='\u@private:\w\$ '" >> .bashrc

# add test page
RUN echo "<html><body><h1>It works too!</h1></body></html>" > /var/www/localhost/htdocs/index.html

# set default route
COPY set_route.sh ./

# start apache and sshd (executed at container startup)
ENTRYPOINT rc-service apache2 stop; rc-service apache2 start; rc-service sshd stop; rc-service sshd start; sleep infinity

