#!/bin/bash
#permette il caricamento del database in mysql

#avvia il servizio di mysql server
sudo /etc/init.d/mysql start

echo "Inserire di seguito la password di mysql per completare l'installazione"
mysql -u root -p < setteemezzo_standalone.sql
echo "Congratulazioni! L'installazione di Sette e Mezzo e' terminata!"
sleep 4
