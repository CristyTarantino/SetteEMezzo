package settemezzo.server;

import java.io.*; 
import java.net.*;

import settemezzo.mazzo.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp ServeOneClient</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Consente la gestione
 * dell'interazione tra il client, il suo avversario e la logica della partita </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class ServeOneClient extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Partita partita;

	/**
	 * Consente la creazione di un thread per la gestione
	 * di una partita del client
	 * @param socket La socket del client
	 * @throws IOException generato nel caso di impossibilita' 
	 * di connettersi con un client
	 */
	public ServeOneClient(Socket aSocket) throws IOException {
		socket = aSocket;

		// Creazione del canale di input
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Creazione del canale di output con l'opzione di flush attiva
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())), true);

		// Avvio del thread
		start();
	}

	@Override
	public void run() {
		try 
		{	
			System.out.println("ServeOneClient " + getName() + " is running ...");

			while(true)
			{
				/*
				 * Lettura di una riga dal canale di input e successiva
				 * suddivisione in stringhe secondo il carattere "&".
				 * Il metodo e' usato per ricevere dati assieme alla richiesta
				 */
				String[] richiesta =  in.readLine().split("&");
				
				// Isoliamo la richiesta del client
				String str = richiesta[0];

				// Individuazione della richiesta
				if(str.equals("start"))
				{
					System.out.println(">>> start " + richiesta[1]);
					
					/*
					 * Viene creata una partita passando al costruttore di questa
					 * il mazzo e la carta rappresentante la matta per quel mazzo.
					 * Infine, si avvia la partita
					 */
					partita = new Partita(this, new MazzoNapoletano(), 
							MazzoNapoletano.MATTA);
					partita.avvia();
					partita.setNome(richiesta[1]);
				}
				else if(str.equals("setNomeGioc"))
				{
					System.out.println(">>> setNomeGioc " + richiesta[1]);
					
					// Viene impostato il nome del giocatore connesso
					partita.setNome(richiesta[1]);
				}
				else if(str.equals("nomeGioc"))
				{
					System.out.println(">>> nomeGioc " +richiesta[1]);

					// Richiesta del nome di un giocatore
					String msg = partita.daiNomeGioc(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("ruoloGioc"))
				{
					// Richiesta del ruolo di un giocatore
					System.out.println(">>> ruoloGioc " + richiesta[1]);
					
					String msg = partita.daiRuoloGioc(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("iniziaMano"))
				{
					// Richiesta di inizio di una mano
					System.out.println(">>> iniziaMano");
					
					// Verifichiamo se sia necessario effettuare lo scambio dei ruoli
					boolean msg = partita.verificaRuoli();
					inviaMsgAlClient(msg);
				}
				else if(str.equals("mattaEstratta"))
				{
					System.out.println(">>> mattaEstratta");
					
					// Richiesta di verifica di estrazione matta
					boolean msg = partita.mattaEstratta();
					inviaMsgAlClient(msg);
				}
				else if(str.equals("cartaCoperta"))
				{
					/*
					 * Richiesta di carta coperta per il giocatore
					 * indicato nella richiesta
					 */
					System.out.println(">>> cartaCoperta " + richiesta[1]);
					
					partita.distribuisciCarteCoperte(richiesta[1]);
				}
				else if(str.equals("creditoGioc"))
				{
					// Richiesta del credito di un giocatore
					System.out.println(">>> in creditoGioc " + richiesta[1]);
					
					String msg = partita.daiCreditoGioc(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("invioPuntata"))
				{
					// Richiesta di impostazione della puntata dello sfidante client
					System.out.println(">>> invioPuntata " + richiesta[1]);
					
					partita.setPuntataGioc(richiesta[1]);
				}
				else if(str.equals("puntataAvversario"))
				{
					// Richiesta della puntata dell'avversario
					System.out.println(">>> puntataAvversario");
					
					String msg = partita.daiPuntataAvversario();
					inviaMsgAlClient(msg);
				}
				else if(str.equals("altraCarta"))
				{
					// Richiesta di un'altra carta da parte del giocatore client
					System.out.println(">>> altraCarta");
					
					partita.daiCarta();
				}
				else if(str.equals("punteggioGioc"))
				{
					// Richiesta del punteggio di un giocatore
					System.out.println(">>> punteggioGioc " + richiesta[1]);
					
					String msg = partita.daiPunteggio(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("stopCarte"))
				{
					/*
					 * Il giocatore client non necessita di altre carte
					 * e lascia il turno al giocatore avversario
					 * (ha scelto di "stare" con le sue carte)
					 */
					System.out.println(">>> stopCarte");
					partita.giocaAvversario();
				}
				else if(str.equals("valutaVincitore"))
				{
					// Richiesta di determinazione del vincitore
					System.out.println(">>> valutaVincitore");
					
					String msg = partita.valutaVincitore();
					inviaMsgAlClient(msg);
				}
				else if(str.equals("resetta"))
				{
					/*
					 * Richiesta di finalizzazione della mano corrente
					 * che permette il salvataggio nel db, il reset dei giocatori
					 * e la determinazione di continuazione della partita
					 */
					System.out.println(">>> resetta " + richiesta[1]);
					
					String msg = partita.fineMano(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("login"))
				{
					// Richiesta di autenticazione dell'utente
					System.out.println(">>> login " + richiesta[1] + " " + richiesta[2]);
					
					String msg = Partita.login(richiesta[1], richiesta[2]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("salvaPartita"))
				{
					// Richiesta di salvataggio partita
					System.out.println(">>> salvaPartita " + richiesta[1]);
					
					boolean msg = partita.salva(richiesta[1]);
					inviaMsgAlClient(msg);
				}
				else if(str.equals("recuperaSalvataggi"))
				{
					// Richiesta di recupero salvataggi
					System.out.println(">>> recuperaSalvataggi " + richiesta[1]);
					
					String[] msg = Partita.recuperaSalvataggi(richiesta[1]);
					
					for (String salvataggio : msg)
						inviaMsgAlClient(salvataggio);
					
					inviaMsgAlClient("stop");
				}
				else if(str.equals("caricaPartita"))
				{
					// Richiesta di caricamento partita
					System.out.println(">>> caricaPartita " + richiesta[1]);
					
					partita = new Partita(this, new MazzoNapoletano(), 
							MazzoNapoletano.MATTA);
					boolean msg = partita.carica(richiesta[1]);
					
					inviaMsgAlClient(msg);
				}
				else if(str.equals("finePartita"))
				{
					// Richiesta di fine partita
					System.out.println(">>> finePartita");
					break;
				}
			}
		} 
		catch (NullPointerException e) 
		{
			System.err.println("Connessione interrotta");
		}
		catch (IOException e) 
		{
			System.err.println("Errore I/O!");
		}
		finally
		{
			try 
			{
				// Chiusura della socket del client
				socket.close();
				System.out.println("Chiusura socket/Fine thread " + getName());
			} 
			catch (IOException e) 
			{
				System.err.println("Impossibile chiudere la socket del client!");
			}
		}
	}
	
	/**
	 * Consente l'invio di informazioni al client
	 * sotto forma di stringa.
	 * Ogni oggetto passato viene convertito in stringa
	 * tramite l'ausilio di <code>String.valueOf()</code>
	 * @param msg l'informazione da inviare al client.
	 */
	public void inviaMsgAlClient(Object msg) {
		System.out.println("<<< inviaMsgAlClient: " + msg);
		// La conversione in stringa avviene automaticamente in PrintWriter
		out.println(msg);
	}
}