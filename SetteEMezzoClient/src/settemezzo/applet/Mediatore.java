package settemezzo.applet;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import settemezzo.client.GiocatoreClient;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Mediatore</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che gestisce gli
 * eventi dell'interfaccia grafica </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Mediatore implements ActionListener, ItemListener, 
													KeyListener, FocusListener {

	private final static String SFIDANTE = "sfidante";
	private final static String BANCO = "banco";
	private final static String GIOCATORE = "giocatore";
	private final static String AVVERSARIO = "avversario";
	private String email = null;
	private String password = null;
	private String nickname = null;

	private MainApplet applet;
	private GiocatoreClient giocatore;
	private String[] msgErr = {"Impossibile avviare la partita!", 
							"Spiacenti! La connessione si e' interrotta! ",
							"Si prega di riprovare piu' tardi!",
							"Impossibile modificare l'aspetto dell'applet!"};

	/**
	 * Costruisce un gestore di eventi
	 * @param applet l'applet della quale gestire gli eventi
	 * @param l'indirizzo ip del server
	 * @param la porta in ascolto del server
	 */
	public Mediatore(MainApplet applet, String porta) {
		this.applet = applet;

		// Creare un oggetto che gestisce la comunicazione col serve
		try 
		{
			giocatore = new GiocatoreClient(this.applet.getCodeBase().getHost(), Integer.parseInt(porta));
		} 
		catch (Exception e) 
		{
			applet.stampaErr(msgErr[0],msgErr[2]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		// Salva la stringa di comando associato all'evento scatenato
		String testo = evento.getActionCommand();

		// Se la stringa contiene nuova partita
		if(testo.indexOf("Nuova") != -1)
		{
			// Prova a...
			try
			{
				iniziaPartita(true);
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1], msgErr[2]);
			}
		}
		else if(testo.indexOf("Punta") != -1)
		{
			/*
			 * Chiede all'utente, solo quando e' sfidante, 
			 * l'importo da puntare
			 * Se l'utente e' banco il pulsante e' disabilitato
			 */
			int puntata = applet.chiediPuntata();

			// Invia la puntata al server
			giocatore.inviaPuntataAlServer(puntata);

			// Disabilita il pulsante che ha premuto
			applet.abilitaPunta(false);

			// Stampa la puntata
			applet.stampaPuntata(puntata, GIOCATORE);

			// Abilita il pulsante Altra Carta
			applet.abilitaAltraCarta(true);
		}
		else if(testo.indexOf("Altra Carta") != -1)
		{
			// Se preme tale pulsante lo disabilita
			applet.abilitaAltraCarta(false);

			try
			{
				// Chiede al server le info relative ad un'altra carta
				HashMap<String,String> carta = giocatore.chiediAltraCarta();

				// Stampa tali informazioni
				applet.stampaCarta(
						carta.get("seme"), 
						carta.get("tipoCarta"), 
						carta.get("tipoGioc") );

				/*
				 * Se il punteggio ottenuto e' inferiore a 7,5 abilita il pulsante
				 * altrimenti rimarra' disabilitato
				 */
				if(chiediPunteggio(GIOCATORE))
					applet.abilitaAltraCarta(true);
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1],msgErr[2]);

				applet.abilitaNuovaPartita(true);
			}
		}
		else if(testo.indexOf("Stai") != -1)
		{
			try
			{
				// quando il giocatore preme stai lo disabilita
				applet.abilitaAltraCarta(false);

				// Se il giocatore e' sfidante fa giocare l'avversario
				if(applet.daiRuolo().equalsIgnoreCase(SFIDANTE))	
					giocaAvversario();

				// Compie le azioni conclusive di una mano
				fineMano();
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1],msgErr[2]);

				applet.abilitaNuovaPartita(true);
			}
		}
		else if(testo.indexOf("Altra Mano") != -1)
		{
			try
			{
				/*
				 * Quando l'utente clicca su Altra Mano, disabilita tale pulsante,
				 * resetta i campi relativi agli utenti nell'applet, 
				 * e iniza una nuova mano
				 */
				applet.abilitaAltraMano(false);
				applet.abilitaSalvaPartita(false);
				applet.resetCampi();
				mano();
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1],msgErr[2]);
			}
		}
		else if(testo.indexOf("Informazioni") != -1)
		{
			// Chiama un metodo di applet che visualizza i credits
			applet.informazioni();
		}
		else if(testo.indexOf("Guida") != -1)
		{
			// Chiama un metodo di applet che visualizza la guida in linea
			applet.guida();
		}   
		else if(testo.indexOf("OK") != -1)
		{
			// Quando l'utente clicca ok nel JDialog per i credits, lo chiude
			applet.infoDialog.dispose();
		}
		else if(testo.indexOf("Storico") != -1)
		{
			try
			{
				boolean esitoLogin = true;
				
				// Se l'utente non si e' loggato, viene visualizzata la finestra di login
				if(email == null && password == null)
					esitoLogin = login("Per visualizzare lo storico e' necessario autenticarsi");
				
				/*
				 * Se l'utente si e' autenticato, richieda la data
				 * e apre la pagina dello storico in una nuova finestra del browser 
				 */
				if(esitoLogin)
				{
					String data = applet.chiediData();
					if(data != null)
					{
						String urlString = applet.getCodeBase().toString() + "Storico.jsp?email=" + email + "&data=" + data;
						applet.getAppletContext().showDocument(new URL(urlString), "_blank");
					}
				}
				
				// Cerca di visualizzare il nickname dell'utente nell'applet 
				applet.stampaNome(nickname, GIOCATORE);
				giocatore.daiNomeAlServer(nickname);
			} 
			catch (MalformedURLException e) 
			{
				applet.stampaErr("Siamo Spiacenti!","Il servizio e' al momento non disponibile!");
			}
			catch (NullPointerException e)
			{
				/* 
				 * Se non e' possibile stampare il nome del giocatore
				 * poiche' non ha selezionato la voce nuova partita, e di conseguenza
				 * non sono stati creati i tavoli dei giocatori ove normalmente
				 * vengono visualizzati i relativi nomi (login durante l'esecuzione della partita)
				 */
				return;
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1], msgErr[2]);
			}	
		}
		else if(testo.indexOf("Carica") != -1)
		{
			try
			{
				boolean esitoLogin = true;
				
				// Se l'utente non si e' loggato, viene visualizzata la finestra di login
				if(email == null && password == null)
					esitoLogin = login("Per caricare una partita e' necessario autenticarsi");
				
				/*
				 * Se l'utente si e' autenticato, vengono recuperati i suoi salvataggi
				 * e viene mostrata una finestra di scelta 
				 */
				if(esitoLogin)
				{
					Object[] salvataggi = giocatore.recuperaSalvataggi(email);
					
					// Se sono presenti dei salvataggi visualizzali
					if(salvataggi.length != 0)
					{
						String salvataggio = applet.chiediSalvataggio(salvataggi);
						
						/*
						 * Se l'utente ha selezionato un salvataggio
						 * e se il caricamento e' andato a buon fine,
						 * avvia la partita caricata 
						 */
						if(salvataggio != null && giocatore.carica(email, salvataggio))
							iniziaPartita(false);
					}
					else // altrimenti informa l'utente
						applet.stampaMsg("Nessun salvataggio disponibile!");
				}
				
			}
			catch (NullPointerException e)
			{
				/* 
				 * Se non e' possibile stampare il nome del giocatore
				 * poiche' non ha selezionato la voce nuova partita, e di conseguenza
				 * non sono stati creati i tavoli dei giocatori ove normalmente
				 * vengono visualizzati i relativi nomi (login durante l'esecuzione della partita)
				 */
				return;
			}
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1], msgErr[2]);
			}		
		}
		else if(testo.indexOf("Salva") != -1)
		{
			try
			{
				boolean esitoLogin = true;
				
				// Se l'utente non si e' loggato, viene visualizzata la finestra di login
				if(email == null && password == null)
				{
					esitoLogin = login("Per salvare una partita e' necessario autenticarsi");
					applet.stampaNome(nickname, GIOCATORE);
				}

				/*
				 * Se l'utente si e' autenticato, viene inviato il nome al server,
				 * viene caricata la partita e visualizzato l'esito dell'operazione
				 */
				if(esitoLogin)
				{
					giocatore.daiNomeAlServer(nickname);
					
					// Salvataggio della partita e visualizzazione dell'esito
					if(giocatore.salva(email))
						applet.stampaMsg("Salvataggio avvenuto con successo!!!");
					else
						applet.stampaErr("Siamo spiacenti!", "Non e' stato possibile salvare la partita!");
				}
			} 
			catch (Exception e) 
			{
				applet.stampaErr(msgErr[1], msgErr[2]);
			}					
		}
		else if(testo.indexOf("Login") != -1)
		{
			boolean esitoLogin = true;
			try
			{
				// Viene visualizzata la finestra di login
				esitoLogin = login("Login di accesso alle operazioni avanzate");
				
				/*
				 * Se l'utente si e' autenticato, viene visualizzato il nome 
				 * viene inviato il nome al server 
				 * e visualizzato l'esito dell'operazione
				 */
				if(esitoLogin)
				{
					applet.stampaNome(nickname, GIOCATORE);
					giocatore.daiNomeAlServer(nickname);
					applet.stampaMsg("Ora sei autenticato!");
				}
			} 
			catch (NullPointerException e)
			{
				/* 
				 * Se non e' possibile stampare il nome del giocatore
				 * poiche' non ha selezionato la voce nuova partita, e di conseguenza
				 * non sono stati creati i tavoli dei giocatori ove normalmente
				 * vengono visualizzati i relativi nomi (login durante l'esecuzione della partita)
				 */
				if(esitoLogin)
					applet.stampaMsg("Ora sei autenticato!");
			}
			catch (IOException e) 
			{
				applet.stampaErr(msgErr[1], msgErr[2]);
			}
		}
		else if(testo.indexOf("Registrazione") != -1)
		{
			// Apre il form di registrazione in una nuova finestra del browser
			try 
			{
				String urlString = applet.getCodeBase().toString() + "FormRegistrazione.html";
				applet.getAppletContext().showDocument(new URL(urlString), "_blank");
			} 
			catch (MalformedURLException e) 
			{
				applet.stampaErr("Siamo Spiacenti!", "Il servizio e' al momento non disponibile!");
			}
			
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evento) 
	{
		// Identifica il JRadioButton che ha scatenato l'evento
		JRadioButtonMenuItem radioButtonMi = (JRadioButtonMenuItem) evento.getSource();

		if (radioButtonMi.isSelected()) 
		{
			// Ricava il valore associato alla chiave "nomeLookAndFeel" per quel radioButton
			UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo) 
										radioButtonMi.getClientProperty("nomeLookAndFeel");

			// Cerca di modificare l'apetto dell'applet
			try 
			{
				/*
				 * Prende il valore associato alla chiave "jgoodies.headerStyle" 
				 * per il menu aspetto, che e' il tema selezionato dall'utente
				 */
				applet.menuAspetto.putClientProperty("jgoodies.headerStyle", "Both");

				// Imposta l'aspetto selezionato dell'utente
				UIManager.setLookAndFeel(info.getClassName());

				// Aggiorna l'aspetto dei componenti dell'applet
				SwingUtilities.updateComponentTreeUI(applet);
				if(applet.help != null)
					SwingUtilities.updateComponentTreeUI(applet.help);
			} 
			catch (Exception e) 
			{
				applet.stampaMsg(msgErr[3]);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Se e' stato premuto il stato Invio, simula la pressione del pulsante
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			((JButton)(e.getSource())).doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void focusGained(FocusEvent e) {
		// Cambia il colore del testo del bottone che ha il focus
		applet.buttonFocusOn((JButton)e.getSource(), true);
	}

	@Override
	public void focusLost(FocusEvent e) {
		// Cambia il colore del testo del bottone che ha perso il focus
		applet.buttonFocusOn((JButton)e.getSource(), false);
	}

	/**
	 * Comunica al server la terminazione della partita
	 */
	public void finePartita() {
		try
		{
			giocatore.finePartita();
		}
		catch(Exception e)
		{
			return;
		}
	}

	/*
	 * Mostra la finestra di autenticazione e memorizza i dati di accesso immessi
	 * @param msg Il messaggio da presentare nella finestra di autenticazione
	 * @return true se l'utente si e' loggato, false se ha annullato l'operazione
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 */
	private boolean login(String msg) throws IOException {
		String[] login = applet.login(msg);
		
		if(login != null)
		{	
			String nickname = giocatore.login(login);

			// Se l'utente si e' autenticato...
			if(!nickname.equals("null"))
			{
				//... salva i dati di accesso
				this.nickname = nickname;
				email = login[0];
				password = login[1];
				//... disabilita le voci relative al login e alla registrazione
				applet.abilitaLogin(false);
				applet.abilitaRegistrazione(false);
				return true;
			}
			else
			{
				applet.stampaErr("La combinazione di indirizzo di posta " +
						"elettronica e password non e' corretta.", 
				"Assicurati che BLOC MAIUSC non sia attivato");
				return login(msg);
			}
		}
		else
			return false;
	}

	/*
	 * Consente l'avvio di una nuova partita 
	 * o il caricamento di una precedentemente salvata 
	 * @param newPartita true se e' l'utente desidera avviare una nuova partita,
	 * false se invece desidera caricarne una
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private void iniziaPartita(boolean newPartita) 
							throws IOException, NullPointerException {
		// Crea il tavolo e ne pulisce i campi se eventualmente sono presenti
		applet.creaTavolo();
		applet.resetCampi();
	
		// Disabilita le voci dal menu' partita relative al salva e carica
		applet.abilitaNuovaPartita(false);
		applet.abilitaCaricaPartita(false);
	
	
		// Chiede all'utente il nome
		String nome;
	
		if(nickname == null)
			nome = applet.chiediTesto("\n Inserisci il tuo nome: ");
		else
			nome = nickname;
	
		if(newPartita)
			// Lo invia al server tramite l'istanza GiocatoreClient
			giocatore.nuovaPartita(nome);

	
		// Visualizza il nome del giocatore nel tavolo inferiore dell'applet
		applet.stampaNome(nome, GIOCATORE);
	
		/*
		 * Chiede al server,attraverso l'istanza GiocatoreClient, 
		 * il nome dell'avversario
		 * e lo visualizza nel tavolo superiore dell'applet
		 */
		applet.stampaNome(giocatore.chiediNomeAlServer(AVVERSARIO), AVVERSARIO);
	
		/*
		 * Chiede al server, attraverso l'istanza GiocatoreClient, 
		 * il nome dell'avversario
		 * e lo visualizza nel tavolo superiore dell'applet
		 */
		HashMap<String, String> ruoloGioc = giocatore.chiediRuoloAlServer(GIOCATORE);
		applet.stampaRuolo(ruoloGioc.get("ruolo"), ruoloGioc.get("tipoGioc"));
		
		HashMap<String, String> ruoloAvv = giocatore.chiediRuoloAlServer(AVVERSARIO);
		applet.stampaRuolo(ruoloAvv.get("ruolo"), ruoloAvv.get("tipoGioc"));
	
		/*
		 * Chiede al server, l'istanza GiocatoreClient, il nome dell'avversario
		 * e lo visualizza nel tavolo superiore dell'applet
		 */
		chiediCredito(GIOCATORE);
		chiediCredito(AVVERSARIO);
	
		// Inizia la mano della partita
		mano();
	}

	/*
	 * Chiede al server il credito del giocatore in esame e lo visualizza 
	 * @param tipoGioc il tipo di giocatore in esame, se giocatore o avversario
	 * @return il credito del giocatore e il tipo del giocatore
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private void chiediCredito(String tipoGioc) 
							throws IOException, NullPointerException {

		HashMap<String, String> creditoGioc = giocatore.chiediCreditoAlServer(tipoGioc);

		applet.stampaCredito(Integer.parseInt(creditoGioc.get("credito")), 
													creditoGioc.get("tipoGioc"));
	}

	/*
	 * Inizia una nuova Mano
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private void mano() throws IOException, NullPointerException  {

		// Contatta il server per cominciare la mano
		boolean scambiaruolo = giocatore.iniziaMano();

		// Se l'utente gli invia istruzioni per effettuare lo swap
		if(scambiaruolo)
		{
			// E' necessario chiedere prima i ruoli dei due giocatori e visualizzarli
			HashMap<String, String> ruoloGioc = giocatore.chiediRuoloAlServer(GIOCATORE);			
			HashMap<String, String> ruoloAvv = giocatore.chiediRuoloAlServer(AVVERSARIO);

			/*
			 * Di conseguenza visualizza, al giocatore, 
			 * un messaggio di avviso di cambiamento del proprio ruolo 
			 */
			if(ruoloGioc.get("tipoGioc").equalsIgnoreCase(GIOCATORE))
				applet.stampaMsg("Ora sei diventato " + ruoloGioc.get("ruolo") + "!!!");
			else
				applet.stampaMsg("Ora sei diventato " + ruoloAvv.get("ruolo") + "!!!");
			
			applet.stampaRuolo(ruoloGioc.get("ruolo"), ruoloGioc.get("tipoGioc"));
			applet.stampaRuolo(ruoloAvv.get("ruolo"), ruoloAvv.get("tipoGioc"));
		}

		/*
		 * Chiede se e' stata estratta la matta e nel caso notifica tale evento
		 * al giocatore, poichè il banco si appresta a mescolare il mazzo 
		 */
		boolean mattaEstratta = giocatore.mattaEstratta();
		if(mattaEstratta)
			applet.stampaMsg("E' stata estratta la Matta! Il mazzo verra' ora mescolato! ");

		// Toglie le vecchie carte dal tavolo
		applet.resetTavoli();

		// Distribuisce le carte scoperte prima allo sfidante poi al banco
		distribuisciCartaCoperta(SFIDANTE);
		distribuisciCartaCoperta(BANCO);

		// Se il giocatore e' sfidante deve puntare
		if(applet.daiRuolo().equalsIgnoreCase(SFIDANTE))
			applet.abilitaPunta(true);
		else
		{
			/*
			 * altrimenti il giocatore e' banco quindi 
			 * fa giocare per primo l'avversario
			 */
			String esito = giocaAvversario();

			// Se l'avversario ha sballato, compie le operazioni di conclusione mano 
			if(esito.equals("sballato"))
				fineMano();
			else
				applet.abilitaAltraCarta(true); // altrimenti gioca il giocatore
		}
	}

	/* 
	 * Chiede al server la carta coperta per il giocatore in esame
	 * visualizzandola nel relativo tavolo e aggiornando il relativo punteggio
	 * @param ruolo Il ruolo del giocatore in esame
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private void distribuisciCartaCoperta(String ruolo) 
							throws IOException, NullPointerException{

		HashMap<String,String> cartaCopGioc = giocatore.distribuisciCartaCoperta(ruolo);

		applet.stampaCartaCoperta(
							cartaCopGioc.get("seme"), 
							cartaCopGioc.get("tipoCarta"), 
							cartaCopGioc.get("tipoGioc") );

		/*
		 * Visualizza per l'avversario il punteggio scoperto,
		 * per il giocatore il punteggio totale
		 */
		applet.stampaPunteggio(
				cartaCopGioc.get("puntiTot"), cartaCopGioc.get("tipoGioc"));		
	}

	/*
	 * Chiede al server il punteggio totalizzato dal giocatore in esame
	 * @param aGioc Il tipo di giocatore, se GIOCATORE o AVVERSARIO
	 * @return true se il punteggio non supera sette e mezzo, false altrimenti
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private boolean chiediPunteggio(String aGioc) 
						throws IOException, NullPointerException{

		boolean continua = false;

		// Chiede al server il punteggio totalizzato dal giocatore e lo visualizza
		HashMap<String,String> punteggio = giocatore.chiediPunteggioAlServer(aGioc);
		applet.stampaPunteggio(punteggio.get("puntiTot"), aGioc);

		/*
		 * Se il giocatore per il quale si richiede 
		 * il calcolo del punteggio e' il giocatore
		 */
		if(aGioc.equals(GIOCATORE))
		{
			String esito = punteggio.get("esito");

			/*
			 * Se ha sballato, lo notifica al giocatore, pone continua a false
			 * e fa giocare l'avversario se il giocatore e' sfidante.
			 * Infine conclude la mano
			 */
			if(esito.equals("sballato"))
			{
				applet.stampaErr("Spiacente, hai SBALLATO!!!", new String());
				continua = false;
				if(applet.daiRuolo().equalsIgnoreCase(SFIDANTE))
					giocaAvversario();
				fineMano();
			}
			/*
			 * Se ha realizzato sette e mezzo, 
			 * lo notifica al giocatore, pone continua a false
			 * e fa giocare l'avversario se il giocatore e' sfidante.
			 * Infine conclude la mano
			 */
			else if(esito.equals("setteMezzo"))
			{
				applet.stampaMsg("Complimenti, hai realizzato Sette e Mezzo!!! ");
				continua = false;
				if(applet.daiRuolo().equalsIgnoreCase(SFIDANTE))	
					giocaAvversario();
				fineMano();
			}
			else
				continua = true; 
			/*
			 * Altrimenti pone continua a true e il giocatore
			 * continua il suo turno
			 */
		}

		return continua;
	}

	/*
	 * Effettua richieste al server circa la giocata dell'avversario
	 * @return true se l'avversario ha realizzato Sette e Mezzo o ha deciso
	 * di non richiedere piu' carte, false se ha sballato
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private String giocaAvversario() throws IOException, NullPointerException {
		boolean stop = false;
		String esito = "";

		// Se il giocatore e' banco stampa la puntata dell'avversario sfidante
		if(applet.daiRuolo().equalsIgnoreCase(BANCO))
			applet.stampaPuntata(Integer.parseInt(giocatore.chiediPuntataAlServer()), AVVERSARIO);

		// Invia al server la richiesta per far passare il turno all'avversario
		giocatore.stopCarte();

		/*
		 * Fintantoche' il server non invia "stop", legge le carte dal buffer d'ingresso
		 * e le visualizza nella schermata del giocatore, 
		 * aggiornando il suo punteggio scoperto
		 */
		while(!stop)
		{
			HashMap<String,String> infoCarteSfid = giocatore.leggiCartaAvversario();
			if(infoCarteSfid.containsKey("esito"))
			{
				stop = true;
				esito = infoCarteSfid.get("esito");
			}
			else
			{
				applet.stampaCarta(infoCarteSfid.get("seme"), infoCarteSfid.get("tipoCarta") , infoCarteSfid.get("tipoGioc"));
				applet.stampaPunteggio(infoCarteSfid.get("puntiTot"), infoCarteSfid.get("tipoGioc"));
			}

		}

		/* 
		 * Se l'avversario ha realizzato sette e mezzo 
		 * oppure ha sballato lo notifica al giocatore
		 */
		if(esito.equals("setteMezzo"))
			applet.stampaMsg(applet.daiNome(AVVERSARIO) + " ha realizzato Sette e Mezzo!!!");
		else if(esito.equals("sballato"))
			applet.stampaMsg(applet.daiNome(AVVERSARIO) + " ha SBALLATO!!!");


		return esito;
	}

	/*
	 * Effettua le operazioni di conclusione mano
	 * @throws IOException Se si presenta un errore di I/O nella comunicazione col server
	 * @throws NullPointerException Se non e' possibile visualizzare il componente nell'applet
	 */
	private void fineMano() throws IOException, NullPointerException {
		// Scopre la carta coperta dell'avversario
		applet.scopriCarta();

		// Visualizza il punteggio totale realizzato dall'avversario
		chiediPunteggio(AVVERSARIO);

		// Notifica al giocatore il vincitore della mano
		applet.stampaMsg(giocatore.valutaVincitore());

		// Aggiorna il credito dei giocatori
		chiediCredito(GIOCATORE); 
		chiediCredito(AVVERSARIO);

		// Abilita le voci di menu' relative alla partita
		applet.abilitaNuovaPartita(true);
		applet.abilitaSalvaPartita(true);
		applet.abilitaCaricaPartita(true);

		/*
		 * Invia al server l'informazione di fine operazioni.
		 * Se questo risponde con "giocatore", viene notificata all'utente
		 * la fine del suo credito e disabilitato il pulsante "SalvaPartita";
		 * se risponde con "avversario", viene notificata all'utente
		 * la fine del credito dell'avversario e disabilitato il pulsante "SalvaPartita";
		 * altrimenti abilita "Altra mano".
		 */
		String esitoMano = giocatore.reset(email);
		
		if(esitoMano.equalsIgnoreCase("giocatore"))
		{
			applet.stampaErr("Spiacenti, hai perso la partita! Il tuo credito e' inferiore a zero! ",
								"Il vincitore e' " + applet.daiNome(AVVERSARIO)+ "!");
			applet.abilitaSalvaPartita(false);
		}
		else if(esitoMano.equalsIgnoreCase("avversario"))
		{
			applet.stampaMsg("Complimenti, hai vinto la partita! Il credito di "
								+ applet.daiNome(AVVERSARIO)+ " e' inferiore a zero");
			applet.abilitaSalvaPartita(false);
		}
		else
			applet.abilitaAltraMano(true);
	}
}