#!/bin/sh

mkdir web
mkdir web/config
sudo cp -r ../back-end/ web/back-end/
sudo cp ../config/init_database_new.sql web/config/
sudo cp ../config/init_database_old.sql web/config/
sudo cp ../config/start_services.sh web/config/
sudo cp -r ../db/ web/db/
sudo cp -r ../front-end/ web/front-end
sudo cp -r ../html web/html
sudo cp -r ../old web/old
sudo cp -r ../products web/products

sudo docker build . -t shopping_express

sudo rm -rf web

