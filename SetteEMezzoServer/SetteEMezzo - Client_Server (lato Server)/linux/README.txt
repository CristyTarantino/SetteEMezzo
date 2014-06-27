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


N.B. TUTTI I COMANDI SOTTO SPECIFICATI DOVRANNO ESSERE ESEGUITI 
     APRENDO IL TERMINALE NELLA CARTELLA DI INSTALLAZIONE "SetteEMezzo - Client_Server/linux/"


1. Installare MySQL Server versione 5.0 o superiore;
2. Installare Java JRE versione 1.6 o superiore;
3. Installare Tomcat versione 6.0.20 avviando lo script 
   InstallerTomcat.sh da terminale, nel modo seguente:

	sh InstallerTomcat.sh
	
4. A questo punto, se tutto e' andato per il verso giusto, puntando 
   all'indirizzo http://localhost:8080 si dovrebbe avere 
   la pagina di benvenuto di Tomcat. 

	Se la versione di Tomcat e/o di Java e' diversa da quella suddetta, 
	aprire i file InstallerTomcat.sh e tomcat e sostituire:

	- la stringa "apache-tomcat-6.0.20" con la stringa "apache-tomcat-x.x.xx" 
	dove x.x.xx sta per la versione di Tomcat che si intende installare;

	- la stringa "java-6-sun" con la stringa "java-x-sun" dove x sta per 
	la versione di java che si intende installare;

5. Eseguire lo script Installer.sh, nel modo seguente:

	sh Installer.sh

ed eseguire le istruzioni mostrate.

6. Modificare il file di configurazione "tomcat-users.xml" che si trova in /usr/local/tomcat/conf/
   e aggiungere il proprio ruolo. Per esempio:

<role rolename="manager"/>
<user username="nomeUtente" password="passwordUtente" roles="manager"/>

7. Effettuare il deploy del file JSPMAP.war accedendo alla voce
   Tomcat Manager dalla pagina di benvenuto precedentemente menzionata.


Per avviare l'applicazione lato server di Sette e Mezzo, eseguire lo script avvioServer.sh,
nei modi indicati:

- Metodo 1: 
	sh avvioServer.sh

- Metodo 2:
	chmod +x avvioServer.sh
	./avvioServer.sh
