#!/bin/bash

#avvia il servizio di mysql
sudo /etc/init.d/mysql start

#avvia il servizio di tomcat
sudo /etc/init.d/tomcat start

#avvio il file jar del gioco
java -jar SetteEMezzo_Server.jar