#!/bin/sh

WEB_DOMAIN_NAME=web-dev.dynamic-dns.net

mkdir web
mkdir web/config
sudo cp -r ../back-end/ web/back-end/
sudo cp ../config/init_database_new.sql web/config/
sudo cp ../config/init_database_old.sql web/config/
sudo cp ../config/start_services.sh web/config/
sudo cp ../config/config_mysql.sh web/config/
sudo cp -r ../db/ web/db/
sudo cp -r ../front-end/ web/front-end
sudo cp -r ../html web/html
sudo cp -r ../old web/old
sudo cp -r ../products web/products

sudo docker rm shopping_express_container
sudo docker build . -t shopping_express

sudo rm -rf web

sudo docker create --name shopping_express_container -p 80:80 -p 443:443 shopping_express
#sudo docker start shopping_express_container
#sudo docker exec shopping_express_container "/bin/bash -c certbot --apache --non-interactive --agree-tos --register-unsafely-without-email --redirect --domain ${WEB_DOMAIN_NAME}"
#sudo docker stop shopping_express_container

