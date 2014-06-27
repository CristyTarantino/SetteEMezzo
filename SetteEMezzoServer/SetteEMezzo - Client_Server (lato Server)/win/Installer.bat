@echo off
REM avvio il servizio di mysql
net start mysql

REM caricamento dello script del database
echo Inserire di seguito la password di mysql per completare l'installazione
mysql -u root -p < setteemezzo_server.sql

echo Congratulazioni! Installazione di Sette e Mezzo e' terminata!
pause