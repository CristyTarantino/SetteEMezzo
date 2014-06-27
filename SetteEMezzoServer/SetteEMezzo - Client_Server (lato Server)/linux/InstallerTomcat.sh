#!/bin/bash

# estrazione del server tomcat
tar xzfv apache-tomcat-6.0.20.tar.gz

# copia del server
sudo cp -r apache-tomcat-6.0.20 /usr/local/

#creazione link simbolico
sudo ln -s /usr/local/apache-tomcat-6.0.20/ /usr/local/tomcat

# copia dello script per start/stop del server
sudo cp tomcat /etc/init.d/

sudo chmod 755 /etc/init.d/tomcat
sudo update-rc.d tomcat defaults

# avvio del server
sudo /etc/init.d/tomcat start