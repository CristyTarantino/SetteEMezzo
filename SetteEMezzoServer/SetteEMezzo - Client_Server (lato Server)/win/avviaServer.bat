@echo off
REM avvio il servizio di mysql
net start mysql

REM avvio il servizio di tomcat
net start tomcat6

REM avvio il nostro server
java -jar SetteEMezzo_Server.jar