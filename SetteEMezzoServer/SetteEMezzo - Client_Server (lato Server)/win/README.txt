 ================================================================
		 ______   __  __                   
		|____  | |  \/  |                  
		    / /__| \  / | ___ ___________  
		   / / _ \ |\/| |/ _ \_  /_  / _ \ 
		  / /  __/ |  | |  __// / / / (_) |
		 /_/ \___|_|  |_|\___/___/___\___/         
														
 ================================================================
       Autori in ordine alfabetico: Tarantino - Turturo     
 ================================================================

Guida all'installazione dell'applicazione Sette e Mezzo lato Server:


N.B. TUTTI I FILE BAT SOTTO SPECIFICATI DOVRANNO ESSERE ESEGUITI 
     APRENDO CON DIRITTI AMMINISTRATIVI NELLA CARTELLA DI INSTALLAZIONE "SetteEMezzo - Client_Server/win/"


1. Installare MySQL Server versione 5.0 o superiore;
2. Installare Java JRE versione 1.6 o superiore;
3. Installare Tomcat versione 6.0.20 avviando il file
	eseguibile "apache-tomcat-6.0.20.exe"
   
4. A questo punto, se tutto e' andato per il verso giusto, puntando 
   all'indirizzo http://localhost:8080 si dovrebbe avere 
   la pagina di benvenuto di Tomcat. 

5. Eseguire lo script Installer.bat, eseguendo un doppio click sul file
	ed eseguire le istruzioni mostrate.

6. Modificare il file di configurazione "tomcat-users.xml", che si trova nella cartella "conf"
	presente nella directory di installazione del web server Tomcat, e aggiungere il proprio ruolo. 
	Per esempio:

<role rolename="manager"/>
<user username="nomeUtente" password="passwordUtente" roles="manager"/>

7. Effettuare il deploy del file SetteEMezzo.war accedendo alla voce
   Tomcat Manager dalla pagina di benvenuto precedentemente menzionata.


Per avviare l'applicazione lato server di Sette e Mezzo, eseguire lo script avvioServer.bat.

N.B. Qualora il server Tomcat non dovesse avviarsi, inserire la libreria msvcr71.dll della jre, nella cartella bin
	di Tomcat.