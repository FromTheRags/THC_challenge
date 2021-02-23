#!/bin/sh

mysqld_safe &
mysqld_safe --user=mysql --datadir=/var/lib/mysql_bis --port=3307 --socket=/var/run/mysqld/mysqld_bis.sock --pid-file=/var/run/mysqld/mysqld_bis.pid &
sleep 1
mysql < /var/www/config/init_database_new.sql
mysql --execute "ALTER USER 'root'@'localhost' IDENTIFIED BY 'unbonmotdepassepourunebasededonneesmysqlenfinmariadbplutot'"
mysql --socket=/var/run/mysqld/mysqld_bis.sock < /var/www/config/init_database_old.sql
mysql --socket=/var/run/mysqld/mysqld_bis.sock --execute "ALTER USER 'root'@'localhost' IDENTIFIED BY 'unabuenacontrasenaparaprotegerefectivamenteunabasededatosmariadb'"
apache2ctl -D FOREGROUND
