package settemezzo.client;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp GiocatoreClient</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che gestisce i 
 * messaggi tra il client ed il server, lato client </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class GiocatoreClient {
	// Stream di dati bufferizzato utilizzato per ricevere messaggi dal server
	private BufferedReader in;
	
	// Stream di dati utilizzato per inviare messaggi al server
	private PrintWriter out;

	// Rappresenta l'indirizzo IP a cui il client deve collegarsi
	private InetAddress addr = null;
	
	/*
	 * Rappresenta la combinazione ottenuta dall'indirizzo IP piu' la porta
	 * sulla quale il server e' in ascolto, e che il client 
	 * utilizza per inizializzare la connessione
	 */
	private Socket socket = null;
	
	/**
	 * Crea un gestore lato client di messaggi tra client e server
	 * @param server Il server che fornisce il servizio		
	 * @param port La porta sulla quale il server e' in ascolto
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante la creazione della socket
	 */
	public GiocatoreClient(String server, int port) throws IOException {
		// Creazione di un indirizzo IP
		addr = InetAddress.getByName(server);
		socket = new Socket(addr, port);
		openConnection();
	}
	
	/**
	 * Invia un messaggio al server circa l'inizializzazione di una nuova partita
	 * @param nomeGioc Il nome del giocatore
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public void nuovaPartita(String nomeGioc) throws IOException {
		taskOut("start"+ "&" + nomeGioc);
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta 
	 * per iniziare una mano della partita
	 * @return true se bisogna effettuare uno scambio dei ruoli tra i giocatori,
	 * false altrimenti
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public boolean iniziaMano() throws IOException {
		return Boolean.parseBoolean((String)taskIO("iniziaMano"));
	}

	/**
	 * Invia un messaggio al server circa la richiesta del nome del'avversario
	 * @param tipoGioc Il tipo di giocatore, se avversario o giocatore
	 * @return il nome del giocatore in esame
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public String chiediNomeAlServer(String tipoGioc) throws IOException {
		return (String) taskIO("nomeGioc" + "&" + tipoGioc);
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta del ruolo del giocatore in esame
	 * @param tipoGioc Il tipo di giocatore, se avversario o meno
	 * @return il ruolo ed il tipo del giocatore in esame
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> chiediRuoloAlServer(String tipoGioc) throws IOException {
		return mapping((String) taskIO("ruoloGioc" + "&" + tipoGioc));
	}

	/**
	 * Invia un messaggio al server circa la richiesta del credito del giocatore in esame
	 * @param tipoGioc Il tipo di giocatore, se avversario o meno
	 * @return il credito ed il tipo del giocatore in esame
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> chiediCreditoAlServer(String tipoGioc) throws IOException {
		return mapping((String) taskIO("creditoGioc" + "&" + tipoGioc));
	}
	
	/**
	 * Invia un messaggio al server circa l'informazione 
	 * sullo stato della matta (estratta o non estratta)
	 * @return true se la matta e' stata estratta,
	 * false altrimenti
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public boolean mattaEstratta() throws IOException {
		return Boolean.parseBoolean((String)taskIO("mattaEstratta"));
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta di informazioni relative alla
	 * carta coperta del giocatore in esame
	 * @param tipoGioc Il tipo di giocatore, se avversario o meno
	 * @return il tipo del giocatore a cui viene distribuita la carta,
	 * i punti totali o i punti scoperti a seconda del giocatore in esame,
	 * e le informazioni relative alla carta distribuita
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> distribuisciCartaCoperta(String tipoGioc) throws IOException{
		String carta = (String)taskIO("cartaCoperta" + "&" + tipoGioc);
		return mapping(carta);
	}

	/**
	 * Invia un messaggio al server circa la puntata del giocatore
	 * @param puntata La puntata del giocatore
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public void inviaPuntataAlServer(int puntata) {
		taskOut("invioPuntata" + "&" + puntata);
	}

	/**
	 * Invia un messaggio al server circa la richiesta di informazioni relative alla
	 * carta richiesta dal giocatore
	 * @return il tipo del giocatore a cui viene distribuita la carta,
	 * punti totali o punti scoperti a seconda del giocatore in esame,
	 * e le informazioni relative alla carta distribuita
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> chiediAltraCarta() throws IOException {
		String altraCarta = (String)taskIO("altraCarta");
		return mapping(altraCarta);
	}

	/**
	 * Invia un messaggio al server circa la richiesta 
	 * del punteggio totalizzato sino a quel momento dal giocatore in esame
	 * @param tipoGioc Il tipo di giocatore, se avversario o meno
	 * @return il punteggio totalizzato dal giocatore in esame e l'esito circa
	 * la sua mano (se ha sballato, se ha realizzato sette e mezzo, 
	 * se puo' proseguire il suo turno)
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> chiediPunteggioAlServer(String tipoGioc) throws IOException {
		String punteggio = (String)taskIO("punteggioGioc" + "&" + tipoGioc);
		return mapping(punteggio);
	}

	/**
	 * Invia un messaggio al server circa la richiesta di procedere nelle
	 * operazioni conclusive della mano
	 */
	public void stopCarte() {
		taskOut("stopCarte");
	}

	/**
	 * Invia un messaggio al server circa la richiesta 
	 * di valutazione del vincitore della mano
	 * @return il vincitore della mano
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public String valutaVincitore() throws IOException {
		return (String) taskIO("valutaVincitore");
	}

	/**
	 * Invia un messaggio al server circa la richiesta se poter continuare il gioco o meno
	 * @param email L'email del giocatore
	 * @return informazioni circa la possibilita' di proseguire il gioco
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public String reset(String email) throws IOException {
		return (String) taskIO("resetta" + "&" + email);
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta 
	 * della puntata dell'avversario
	 * @return la puntata dell'avversario
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public String chiediPuntataAlServer() throws IOException {
		return (String)taskIO("puntataAvversario");
	}

	/**
	 * Legge dal buffer di input le informazioni riguardanti 
	 * le carte distribuite all'avversario
	 * @return le informazioni riguardanti le carte distribuite 
	 * all'avversario
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public HashMap<String,String> leggiCartaAvversario() throws IOException {
		String string = (String) taskIn();
		return mapping(string); 
	}

	/**
	 * Invia un messaggio al server circa la decisione del giocatore
	 * di terminare la partita e successiva chiusura della socket lato client
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public void finePartita() throws IOException {
		taskOut("finePartita");
		closeConnection();
	}
	
	/**
	 * Invia un messaggio al server circa la decisione del giocatore di autenticarsi
	 * @param login L'array contenente le credenziali richeste per l'autenticazione
	 * @return il nickname dell'utente in esame, se presente nel database, 
	 * altrimenti una stringa vuota
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public String login(String[] login) throws IOException {
		return (String) taskIO("login" + "&" + login[0] + "&" + login[1]);
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta d'impostare il nome del giocatore
	 * @param nickname Il nome del giocatore
	 */
	public void daiNomeAlServer(String nickname) {
		taskOut("setNomeGioc" + "&" + nickname);
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta di salvataggio della partita
	 * @param email L'email del giocatore di cui salvare la partita
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public boolean salva(String email) throws IOException {
		return Boolean.parseBoolean((String)taskIO("salvaPartita" + "&" + email));
	}
	
	/**
	 * Invia un messaggio al server circa la richiesta 
	 * di recuperare i salvataggi del giocatore
	 * @param email L'email del giocatore di cui salvare la partita
	 * @return i nomi dei file dei salvataggi fino a quel momento effettuati
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public Object[] recuperaSalvataggi(String email) throws IOException {
		taskOut("recuperaSalvataggi" + "&" + email);
		boolean stop = false;
		String salvataggio;
		ArrayList<String> salvataggi = new ArrayList<String>();
		
		while(!stop)
		{
			salvataggio = (String) taskIn();
			
			if(salvataggio.equalsIgnoreCase("stop"))
				stop = true;
			else
				salvataggi.add(salvataggio);
		}
		
		return salvataggi.toArray();
	}

	/**
	 * Invia un messaggio al server circa la richiesta di caricamento di una partita
	 * @param email L'email del giocatore in esame 
	 * @param salvataggio In nome della del file di salvataggio contenente la partita
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante l'invio del messaggio al server
	 */
	public boolean carica(String email, String salvataggio) throws IOException {
		return Boolean.parseBoolean((String)taskIO("caricaPartita" + "&" 
														+ email + "/" + salvataggio));
	}

	/*
	 * Crea il canale di comunicazione tra il client ed il server
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante la creazione del canale tra Client e Server 
	 */
	private void openConnection() throws IOException {
		in = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));
		
		// PrintWriter con flush automatico
		out = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(
								socket.getOutputStream())), true);
	}

	/*
	 * Invia messaggi al server tramite il canale precedentemente creato,
	 * e legge dal buffer di input i messaggi che il server ha inviato al client
	 * @param inString Il messaggio da inviare al server
	 * @return il messaggio inviato dal server e letto dal buffer di input 
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante la creazione della socket 
	 */
	private Object taskIO(Object inString) throws IOException {
		taskOut(inString);
		return taskIn();
	}

	/*
	 * Invia al server i messaggi passati come argomento.
	 * Ogni messaggio passato viene convertito in stringa 
	 * tramite l'ausilio di String.valueOf()
	 * @param inString il messaggio da inviare al server
	 */
	private void taskOut(Object inString) {
		out.println(inString);
	}

	/*
	 * Restituisce i messaggi che il server invia al client
	 * @return il messaggio che il server invia al client
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante la lettura dal buffer di input
	 */
	private Object taskIn() throws IOException {
		return in.readLine();
	}

	/*
	 * Chiude la socket di connessione con il server
	 * @throws IOException Se si presenta un errore di I/O 
	 * durante la chiusura della socket 
	 */
	private void closeConnection() throws IOException {
		socket.close();
	}

	/*
	 * Suddivide dapprima una stringa secondo il carattere "&",
	 * e successivamente converte le stringhe ottenute, 
	 * nel formato "chiave=valore", in un HashMap di stringhe
	 * @param string La stringa da splittare
	 * @return hashMap di stringhe con le informazioni richieste
	 */
	private HashMap<String,String> mapping(String string) {
		HashMap<String,String> map = new HashMap<String,String>();
		
			String[] tmp = string.split("&");
		
			for (String coppia : tmp) 
			{
				String[] item = coppia.split("=");
				map.put(item[0], item[1]);
			}
		
		return map;
	}
}