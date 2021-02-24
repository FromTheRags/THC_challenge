#!/bin/sh

mysqld_safe &
mysqld_safe --user=mysql --datadir=/var/lib/mysql_bis --port=3307 --socket=/var/run/mysqld/mysqld_bis.sock --pid-file=/var/run/mysqld/mysqld_bis.pid &
apache2ctl -D FOREGROUND
