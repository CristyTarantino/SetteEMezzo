package settemezzo.server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import settemezzo.mazzo.*;
import settemezzo.giocatore.*;
import settemezzo.util.database.*;
import settemezzo.util.exception.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Partita</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Presenta la logica
 * per la gestione di una partita a Sette e Mezzo tra due giocatori</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Partita implements Serializable{
	private static final long serialVersionUID = 5523336313125719239L;

	/**
	 * Rappresenta il massimo punteggio valido che il giocatore puo' totalizzare  
	 */
	public final static double MAXPUNTI = 7.5;

	/**
	 * Rappresentano il tipo di giocatore impersonato dal client
	 */ 
	public final static String GIOCATORE = new String("giocatore");
	
	/**
	 * Rappresentano il tipo di giocatore impersonato dall'avversario 
	 * del giocatore client
	 */
	public final static String AVVERSARIO = new String("avversario");
	
	/*
	 * Percorso relativo della cartella 
	 * che contiene i salvataggi di ogni giocatore
	 */
	private final static String PATH_UTENTI = "./utenti/";

	private static Carta MATTA;

	private Mazzo mazzoCarte;
	private Giocatore sfidante;
	private Giocatore banco;
	private Date data;
	private ServeOneClient client;
	private boolean mattaEstratta;
	private boolean swap;
	
	/**
	 * Inizializza una nuova partita a Sette e Mezzo 
	 * @param aClient Il client che si connette per giocare
	 * @param mazzo Il mazzo di carte con il quale giocare la partita
	 * @param matta La Carta che assume il valore di matta
	 */
	public Partita(ServeOneClient aClient, Mazzo mazzo, Carta matta) {
		client = aClient;
		MATTA = matta;
		mazzoCarte = mazzo;
		data = new Date();
		mattaEstratta = false;
		swap = false;
		banco = new BancoCPU();
		sfidante = new GiocatoreUmano();
	}

	/**
	 * Restituisce la carta assunta come Matta nel mazzo di carte
	 * @return La matta del mazzo di carte in uso
	 */
	public static Carta daiMatta() {
		return MATTA;
	}

	/**
	 * Avvia una nuova partita a Sette e Mezzo
	 */
	public void avvia() {
		mazzoCarte.mescola();
	}

	/**
	 * Imposta il nome del giocatore umano
	 * @param nome Il nome del giocatore
	 */
	public void setNome(String nome) {
		if(sfidante instanceof GiocatoreUmano)
			sfidante.setNome(nome);
		else
			banco.setNome(nome);
	}

	/**
	 * Restituisce il nome del giocatore desiderato
	 * @param aGioc Il giocatore in esame: 
	 * 			<code>GIOCATORE</code> oppure <code>AVVERSARIO</code>
	 * @return il nome del giocatore desiderato
	 */
	public String daiNomeGioc(String aGioc) {
		if(aGioc.equals(GIOCATORE))
		{
			if(sfidante instanceof GiocatoreUmano)
				return sfidante.daiNome();
			else // banco istanza di GiocatoreUmano
				return banco.daiNome();
		}
		else // tipoGioc uguale a AVVERSARIO
		{
			if( !(sfidante instanceof GiocatoreUmano) )
				return sfidante.daiNome();
			else // banco istanza di BancoCPU
				return banco.daiNome();
		}
	}

	/**
	 * Restituisce il ruolo del giocatore desiderato
	 * @param aGioc Il giocatore di cui si vuole conoscere il ruolo: 
	 * 			<code>GIOCATORE</code> oppure <code>AVVERSARIO</code>
	 * @return stringa contenente le informazioni desiderate 
	 * 			nel formato "chiave=valore" concatenate con il carattere "&"
	 */
	public String daiRuoloGioc(String aGioc) {

		if(aGioc.equals(GIOCATORE))
		{
			if(sfidante instanceof GiocatoreUmano)
				return "tipoGioc="+ GIOCATORE + "&"
						+ "ruolo=" + sfidante.daiRuolo();
			else
				return "tipoGioc="+ GIOCATORE + "&"
						+ "ruolo=" + banco.daiRuolo();
		}
		else // aGioc uguale ad AVVERSARIO
		{
			// Se il banco e' istanza di SfidanteCPU/BancoCPU
			if( !(banco instanceof GiocatoreUmano) )
				return "tipoGioc="+ AVVERSARIO + "&"
						+ "ruolo=" + banco.daiRuolo();
			else
				return "tipoGioc="+ AVVERSARIO + "&"
						+ "ruolo=" + sfidante.daiRuolo();
		}
	}

	/**
	 * Restituisce il credito del giocatore desiderato
	 * @param aGioc Il giocatore di cui si vuole conoscere il ruolo: 
	 * 			<code>GIOCATORE</code> oppure <code>AVVERSARIO</code>
	 * @return stringa contenente le informazioni desiderate 
	 * 			nel formato "chiave=valore" concatenate con il carattere "&"
	 */
	public String daiCreditoGioc(String aGioc) {
		if(aGioc.equals(GIOCATORE))
		{
			if(sfidante instanceof GiocatoreUmano)
				return "tipoGioc="+ GIOCATORE + "&"
						+ "credito=" + sfidante.daiCredito();
			else
				return "tipoGioc="+ GIOCATORE + "&"
						+ "credito=" + banco.daiCredito();
		}
		else // aGioc uguale ad AVVERSARIO
		{
			// Se il banco e' istanza di SfidanteCPU/BancoCPU
			if( !(banco instanceof GiocatoreUmano) )
				return "tipoGioc="+ AVVERSARIO + "&"
						+ "credito=" + banco.daiCredito();
			else
				return "tipoGioc="+ AVVERSARIO + "&"
						+ "credito=" + sfidante.daiCredito();
		}
	}

	/**
	 * Restituisce il punteggio del giocatore desiderato al client
	 * @param aGioc Il giocatore di cui si vuole conoscere il ruolo: 
	 * 			<code>GIOCATORE</code> oppure <code>AVVERSARIO</code>
	 * @return stringa contenente le informazioni desiderate 
	 * 			nel formato "chiave=valore" concatenate con il carattere "&"
	 */
	public String daiPunteggio(String aGioc) {
		if(aGioc.equals(GIOCATORE))
		{
			// Ricavo il punteggio del GiocatoreUmano
			if(sfidante instanceof GiocatoreUmano)
				return daiPunteggioGioc(sfidante);
			else
				return daiPunteggioGioc(banco);
		}
		else // vuol dire che msg e' uguale ad AVVERSARIO
		{
			// Ricavo il punteggio di SfidanteCPU/BancoCPU
			if(sfidante instanceof GiocatoreUmano)
				return daiPunteggioGioc(banco);
			else
				return daiPunteggioGioc(sfidante);
		}
	}

	/**
	 * Imposta la puntata dello sfidante
	 * @param puntata La puntata dello sfidante per la mano corrente
	 */
	public void setPuntataGioc(String puntata) {
		sfidante.setPuntata(Integer.parseInt(puntata));
	}

	/**
	 * Restituisce la puntata dell'avversario
	 * @return la puntata dell'avversario
	 */
	public String daiPuntataAvversario() {
		// Lo SfidanteCPU effettua la puntata
		sfidante.setPuntata(0);

		// Restituiamo la puntata dello sfidante cpu 
		return Integer.toString( sfidante.daiPuntata() );	
	}
	
	/**
	 * Verifica se e' necessario effettuare uno scambio dei ruoli
	 * e ritorna il risultato di tale verifica.
	 * In caso affermativo effettua lo scambio dei ruoli dei giocatori
	 * @return true se e' necessario effettuare lo swap,
	 * 			false altrimenti 
	 */
	public boolean verificaRuoli() {
		int credito = 0;
	
		// Se e' necessario eseguire lo swap ...
		if(swap)
		{
			// ... e se il giocatore umano dovra' essere banco
			if(sfidante instanceof GiocatoreUmano)
			{
				credito = banco.daiCredito();
				banco = sfidante;
				
				// imposto il ruolo del GiocatoreUmano
				banco.setRuolo(Giocatore.BANCO);
				
				sfidante = new SfidanteCPU();
				sfidante.setCredito(credito);
			}
			else // il giocatoreUmano e' il banco
			{
				credito = sfidante.daiCredito();
				sfidante = banco;
	
				// imposto il ruolo del GiocatoreUmano
				sfidante.setRuolo(Giocatore.SFIDANTE);
	
				banco = new BancoCPU();
				banco.setCredito(credito);
			}
			
			System.err.println("\t Swap avvenuto con successo ");
			
			swap = false; // Reset valore swap
			
			return true; // ritorna true, si swap
		}
		else // ritorna false, no swap
		{
			swap = false; // Reset valore swap
			
			return false;
		}
	}
	
	/**
	 * Consente la distribuzione della carta coperta al giocatore
	 * passato come argomento e restituisce una stringa 
	 * con le informazoni sulla carta coperta assegnata
	 * @param aGioc Il giocatore a cui distribuire la carta coperta:
	 * 			<code>Giocatore.SFIDANTE</code> oppure <code>Giocatore.BANCO</code> 
	 */
	public void distribuisciCarteCoperte(String aGioc) {
		if(aGioc.equalsIgnoreCase(Giocatore.SFIDANTE))
			distribuisciCarta(sfidante);
		else
			distribuisciCarta(banco);
	}

	/**
	 * Valuta il vincitore tra i giocatori, aggiornando il credito
	 * dello sfidante e del banco in base ai criteri di gioco.
	 * @return un messaggio con il vincitore della mano
	 */
	public String valutaVincitore() {
		double puntiBanco = banco.daiPuntiTotali();
		double puntiSfid = sfidante.daiPuntiTotali();
		boolean pntDoppia = true;
		String bancoDoppio = "Vince " + banco.daiNome() + "! " + sfidante.daiNome() + " paga DOPPIO!";
		String sfidanteDoppio = "Vince " + sfidante.daiNome() + "! Riceve da " + banco.daiNome() + " una posta DOPPIA!";
		String bancoSemplice = "Vince " + banco.daiNome() + "! " + sfidante.daiNome() + " paga semplice!";
		String sfidanteSemplice = "Vince " + sfidante.daiNome() + "! Riceve da " + banco.daiNome() + " una posta semplice!";

		// Se il banco ha totalizzato sette e mezzo reale 
		if(banco.haReale())
		{
			// HA VINTO IL BANCO
			banco.setVincitore();
			
			/*
			 * Se lo sfidante ha fatto sette e mezzo reale o ha sballato, 
			 * gli viene sottratta una posta semplice
			 */
			if( sfidante.haReale() || sfidante.haSballato() )
			{
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
				
				return bancoSemplice;
			}
			else //altrimenti posta doppia
			{
				sfidante.aggiornaCredito(pntDoppia);
				banco.aggiornaCredito(pntDoppia, sfidante.daiPuntata());
				
				return bancoDoppio;
			}
		}

		// Se il banco ha totalizzato un sette e mezzo illegittimo...
		else if(puntiBanco == MAXPUNTI && banco.daiCarteSulTavolo().size() > 2)
		{
			/*
			 *  ...e lo sfidante ha fatto sette e mezzo con matta e una figura,
			 *  questo riceve una posta doppia e diventa BANCO
			 */
			if(mattaFigura(sfidante))
			{
				sfidante.setVincitore();
				sfidante.aggiornaCredito(pntDoppia);
				banco.aggiornaCredito(pntDoppia, sfidante.daiPuntata());
				
				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
				
				return sfidanteDoppio;
			}
			else
			{
				/*
				 * Altrimenti se ha sballato, se ha fatto sette e mezzo 
				 * o sette e mezzo con piu' di due carte,
				 * oppure ha totalizzato un punteggio inferiore a sette e mezzo,
				 * paga una posta semplice
				 */
				banco.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
				
				return bancoSemplice;
			}
		}


		//Se il banco ha totalizzato un punteggio minore di sette e mezzo 
		else if(puntiBanco < MAXPUNTI)
		{
			/*
			 * Se lo sfidante ha fatto sette e mezzo reale,
			 * riceve posta doppia e diventa BANCO
			 */
			if(sfidante.haReale())
			{
				sfidante.setVincitore();
				sfidante.aggiornaCredito(pntDoppia);
				banco.aggiornaCredito(pntDoppia, sfidante.daiPuntata());

				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
				
				return sfidanteDoppio;
			}
			else if(sfidante.daiCarteSulTavolo().size() > 2 
					&& puntiSfid == MAXPUNTI)
			{
				/*
				 * se lo sfidante ha fatto sette e mezzo con piu' 
				 * di due carte riceve una posta semplice
				 */
				sfidante.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());

				return sfidanteSemplice;
			}
			else if(puntiSfid < MAXPUNTI)
			{
				/*
				 * Se lo sfidante ha totalizzato un punteggio 
				 * minore di sette e mezzo
				 * ed e' minore o uguale a quello del banco, 
				 * gli viene sottratta una posta semplice,
				 * altrimenti la riceve
				 */
				if(puntiBanco >= puntiSfid)
				{
					banco.setVincitore();
					sfidante.aggiornaCredito(!pntDoppia);
					banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
					
					return bancoSemplice;
				}
				else
				{
					sfidante.setVincitore();
					sfidante.aggiornaCredito(!pntDoppia);
					banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());

					return sfidanteSemplice;
				}
			}
			else//se lo sfidante ha sballato gli viene sottratta una posta semplice
			{
				banco.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
				
				return bancoSemplice;
			}
		}

		/*
		 * Se il banco ha sballato e se lo sfidante ha fatto sette e mezzo reale
		 * questo riceve una posta doppia e diventa BANCO
		 * altrimenti riceve una posta semplice
		 */
		else if( banco.haSballato() )
		{ 
			if(sfidante.haReale())
			{
				sfidante.setVincitore();
				sfidante.aggiornaCredito(pntDoppia);
				banco.aggiornaCredito(pntDoppia, sfidante.daiPuntata());
				
				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
				
				return sfidanteDoppio;
			}
			else
			{
				sfidante.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());

				return sfidanteSemplice;
			}
		}
		else 
			return new String();
	}
	
	/**
	 * Determina se carta denotata come Matta
	 * e' stata estratta durante una mano
	 * @return true se la matta e' stata estratta,
	 * 			false altrimenti
	 */
	public boolean mattaEstratta() {
		boolean msg = false;
		
		if(mattaEstratta)
		{	
			msg = true;

			mazzoCarte.mescola();
			mattaEstratta = false;
		}
		
		return msg;
	}
	
	/**
	 * Distribuisce una carta al giocatore 
	 * impersonato dal client 
	 */
	public void daiCarta() {
		if(sfidante instanceof GiocatoreUmano)
			distribuisciCarta(sfidante);
		else
			distribuisciCarta(banco);
	}

	/**
	 * Consente al giocatore avversario 
	 * di poter giocare la sua mano
	 */
	public void giocaAvversario() {
		try 
		{
			/*
			 * Se il client e' lo sfidante e non ha sballato,
			 * consento all'avversario (il banco) di giocare 
			 */
			if(sfidante instanceof GiocatoreUmano && !sfidante.haSballato())
				manoAvversario(banco, sfidante.daiPuntiScoperti());
			/*
			 * Se il client e' il banco faccio giocare lo sfidante 
			 */
			else if(banco instanceof GiocatoreUmano)
				manoAvversario(sfidante, banco.daiPuntiScoperti());

			client.inviaMsgAlClient("esito=ok");
		}
		catch (SetteMezzoException e) 
		{
			client.inviaMsgAlClient("esito=setteMezzo");
		}
		catch (SballatoException e) 
		{
			client.inviaMsgAlClient("esito=sballato");
		}	
	}

	/**
	 * Effettua le operazioni di conclusione della mano
	 * e verifica se il gioco puo' continuare
	 * @param email L'email del giocatore
	 * @return stringa contenente le informazioni desiderate
	 */
	public String fineMano(String email){
		
		// Salvataggio della mano nel database
		salvaManoDB(banco, email);
		salvaManoDB(sfidante, email);

		// Reset di alcuni campi dei giocatori della partita
		banco.reset();
		sfidante.reset();
		
		String msg = "";
		
		// Verifico se entrambi i giocatori hanno credito maggiore di zero
		if(sfidante.daiCredito() > 0 && banco.daiCredito() > 0)
			msg = "continuaGioco";
		else
		{
			/*
			 * Individuo chi non ha un credito sufficiente a giocare
			 * e lo comunico al client
			 */
			if(sfidante.daiCredito() == 0 && sfidante instanceof GiocatoreUmano)
				msg = "giocatore";
			else 
				msg = "avversario";
				
		}
		
		return msg;
	}
	
	/**
	 * Effettua il controllo sull'esistenza dell'utente 
	 * con le credenziali passate come parametri
	 * @param email L'email dell'utente in esame
	 * @param password La password dell'utente in esame
	 * @return il nickname dell'utente in esame, 
	 * se presente nel database, altrimenti
	 * una stringa vuota
	 */
	public static String login(String email, String password) {
		iPartitaDAO utenteTrans = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
		UtenteEntry utente = utenteTrans.getUtente(email, password);
		
		// Recupero info utili sull'utente
		String emailGioc = utente.getEmail();
		String nickname = utente.getNickname();
		
		// Creazione di un file che rappresenta la directory dei salvataggi  
	    File dir = new File(PATH_UTENTI);
	    
	    if(!dir.exists())
			dir.mkdir();
		
		// Creazione di un file che rappresenta la directory dei salvataggi dell'utente  
	    dir = new File(PATH_UTENTI + emailGioc);
		
		if( nickname!= null && !dir.exists())
			dir.mkdir();
		
		return nickname;
	}

	/**
	 * Permette il salvataggio di una partita
	 * @param emailGioc l'email del giocatore in esame
	 * @return true se il salvataggio e' andato a buon fine, false altrimenti 
	 */
	public boolean salva(String emailGioc){		
		String[] files = filesSort(emailGioc);
	    
	    if(files.length >= 10)
	    	new File(PATH_UTENTI + emailGioc + "/" + files[0]).delete();
	    
		SimpleDateFormat data = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
		
		File partita = new File( PATH_UTENTI + emailGioc + "/" + data.format(this.data) + ".dat");
		
		try 
		{	
			// creo l'oggetto file di partita
			FileOutputStream partitaFileOut = new FileOutputStream(partita);

			/*
			 * Istanzio un oggetto dalla classe ObjectOutputStream a cui passo 
			 * l'oggetto file di partita, questo perche' 
			 * ObjectOutputStream e' un oggetto per manipolare i file
			 */
			ObjectOutputStream partitaSer = new ObjectOutputStream(partitaFileOut);

			// serializzo i dati nello stream
			partitaSer.writeObject(sfidante);
			partitaSer.writeObject(banco);
			partitaSer.writeObject(mazzoCarte);
			partitaSer.writeObject(MATTA);
			partitaSer.writeObject(mattaEstratta);
			partitaSer.writeObject(swap);

			// attivo il flusso di dati verso il file contenuto in serPartita
			partitaSer.flush();

			// chiudo il file
			partitaFileOut.close();

			return true;
		} 
		catch (FileNotFoundException e) 
		{
			return false;
		} 
		catch (IOException e) 
		{
			return false;
		}		
	}
	
	
	/**
	 * Permette il recupero dei salvataggi delle partite effetuate dal giocatore
	 * @param emailGioc L'email del giocatore		
	 * @return i nomi dei file relativi ai salvataggi del giocatore
	 */
	public static String[] recuperaSalvataggi(String emailGioc) {
	    return filesSort(emailGioc);
	}

	
	/**
	 * Permette il caricamento di una partita precedentemente salvata
	 * @param partita Il nome della partita da caricare
	 * nel formato dd-MM-yyyy HH:mm:ss (senza estensione) 
	 * @return true se il caricamento e' andato a buon fine, false altrimenti
	 */
	public boolean carica(String partita) {
		try
		{
		    String tmp = partita.replace(":", ".") + ".dat";
		    
			// creo l'oggetto file di partita
			FileInputStream partitaFileIn = new FileInputStream(PATH_UTENTI + tmp);
			
			/*
			 * Istanzio un oggetto dalla classe ObjectOutputStream a cui passo 
			 * l'oggetto file di partita, questo perche' 
			 * ObjectOutputStream e' un oggetto per manipolare i file
			 */
			ObjectInputStream partitaDeser = new ObjectInputStream(partitaFileIn);
			
			// serializzo i dati nello stream
			sfidante = (Giocatore) partitaDeser.readObject();
			banco = (Giocatore) partitaDeser.readObject();	
			mazzoCarte = (Mazzo) partitaDeser.readObject();
			MATTA = (Carta) partitaDeser.readObject();
			mattaEstratta = (Boolean) partitaDeser.readObject();
			swap = (Boolean) partitaDeser.readObject();

			// chiudo il file
			partitaFileIn.close();
			
			return true;
		}
		catch(FileNotFoundException e)
		{
			return false;
		}		
		catch(IOException e)
		{
			return false;
		}
		catch(ClassNotFoundException e)
		{
			return false;
		}
	}

	/*
	 * Consente il recupero ordinato dei nomi dei file con estensione ".dat"
	 * presenti nella cartella del giocatore in esame
	 * @param emailGioc l'email del giocatore che corrisponde 
	 * alla cartella personale di salvataggio delle partite
	 * @return Array dei nomi dei file ordinati per data di creazione
	 */
	private static String[] filesSort(String emailGioc) {
		// Creazione di un file che rappresenta la directory di lavoro corrente
	    File dir = new File(PATH_UTENTI + emailGioc);
	    
	    // Se la directory non esiste viene creata
		if(!dir.exists())
			dir.mkdir();
	    
		/*
	     * Creazione di un'array con i file nella dir corrente.
	     * L'array conterra' solo file la cui estensione e' ".dat"
	     */
	    File[] files = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				/*
				 * Viene estratto il nome del file con nome name,
				 * presente nella directory dir.
				 * Se questa Stringa contiene ".dat", indexOf restituisce la posizione
				 * della prima occorrenza della stringa passata, altrimenti -1.
				 * Se dunque il nome del file contiene ".dat", 
				 * il metodo restituisce true, false altrimenti
				 */
				String file = new File(name).getName();
				return file.indexOf(".dat") != -1;
			}
	    });
	    
	    // Ordinamento dei file attraverso la data di creazione
	    Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				// Calcolo della differenza temporale tra le due date di creazione
	    		long delta = file1.lastModified() - file2.lastModified();
	    		
	    		// file1 creato prima di file2
	    		if (delta < 0) 
	    			return -1;
	    		// file2 creato prima di file1
	    		else if (delta > 0) 
	    			return 1;
	    		
	    		// Creati nello stesso momento
	    		return 0;
			}
	    });
	    
	    String[] stringFiles = new String[files.length];
	    
	    /*
	     * Creazione dell'array contenente i nomi dei file trovati.
	     * I nomi dei file vengono anche formattati e resi 
	     * piu' leggibili da parte dell'utente
	     * Es. 28-06-2009 11.00.20.dat => 28-06-2009 11:00:20 
	     */
	    for (int i = 0; i < stringFiles.length; i++){
	    	stringFiles[i] = files[i].getName();
	    	stringFiles[i] = stringFiles[i].replace(".", ":").substring(0, stringFiles[i].length()-4);
	    }
		return stringFiles;
	}

	/*
	 * Gestisce la mano di un giocatore avversario,
	 * nel nostro caso un giocatore cpu
	 * @param aGioc il giocatore avversario 
	 * @param puntoAvversario il punteggio dell'avversario
	 * @throws SetteMezzoException nel caso il giocatore ha totalizzato sette e mezzo
	 * @throws SballatoException nel caso il giocatore ha sballato
	 */
	private void manoAvversario(Giocatore aGioc, double puntoAvversario) 
					throws SetteMezzoException, SballatoException {
	
		/*
		 * Fintantoche' il giocatore richiede un'altra carta
		 * e non ha sballato ne ha totalizzato sette e mezzo
		 */
		while(aGioc.altraCarta(puntoAvversario))
		{
			//Distribuisce una carta
			distribuisciCarta(aGioc);
	
			// Valuto il punteggio del giocatore
			valutaPunteggio(aGioc);
		}			
	}

	/*
	 * Distribuisce una carta al giocatore aggiornando le sue carte sul tavolo
	 * e restituisce una stringa con le informazioni sulla carta 
	 * @param aGioc Giocatore a cui viene distribuita la carta
	 */
	private void distribuisciCarta(Giocatore aGioc) {
		try
		{
			//Si estrae una carta dal mazzo
			Carta cartaDis = mazzoCarte.carta();
	
			//Si controlla se e' la matta
			if( cartaDis.equals(MATTA) )
				mattaEstratta = true;
	
			//Si consegna la carta al giocatore che quindi la disporra' in carteSulTavolo
			aGioc.setCarteSulTavolo(cartaDis);
	
			String infoCarta = "";
	
			// Visualizza al relativo giocatore la carta estratta
			if(aGioc instanceof GiocatoreUmano)
			{
				infoCarta += "tipoGioc=" + GIOCATORE;
				infoCarta += "&puntiTot=" + aGioc.daiPuntiTotali();
			}
			else
			{
				infoCarta += "tipoGioc=" + AVVERSARIO;
				infoCarta += "&puntiTot=" + aGioc.daiPuntiScoperti();
			}
	
			infoCarta += "&seme=" + cartaDis.daiSeme();
			infoCarta += "&tipoCarta=" + cartaDis.daiTipoCarta();
	
			client.inviaMsgAlClient(infoCarta);
		}
		catch(MazzoTerminatoException e)
		{
			System.err.println(e.getMessage() + "\n Il mazzo verra' ora mescolato!");
	
			/*
			 * Upcasting, carteInUso e' ArrayList di carte, 
			 * sottoclasse della classe Collection di carte
			 * 
			 * Mescoliamo le carte (tranne quelle in uso nella mano)
			 * e richiamiamo il metodo
			 */
			mazzoCarte.mescola(carteInUso());
			distribuisciCarta(aGioc);
		}
	
	}

	/*
	 * Recupera tutte le carte in uso nella una mano corrente
	 * @return un array contenente tutte le carte in uso
	 */
	private ArrayList<Carta> carteInUso() {
		ArrayList<Carta> temp = new ArrayList<Carta>(banco.daiCarteSulTavolo());

		temp.addAll(sfidante.daiCarteSulTavolo());

		return temp;
	}

	/*
	 * Genera una messaggio da inviare al client
	 * contenente il punteggio del giocatore in esame
	 * e la situazione del giocatore: 
	 * 		sballato (se ha sballato),
	 * 		setteMezzo (se ha totalizzato sette e mezzo),
	 * 		ok (se e' ancora in gioco)
	 * @param aGioc Il giocatore in esame
	 * @return il messaggio con le informazioni sul giocatore
	 */
	private String daiPunteggioGioc(Giocatore aGioc) {
		String infoPunteggio = "";

		try 
		{
			// Ricaviamo il punteggio totale del giocatore
			infoPunteggio += "puntiTot=" +aGioc.daiPuntiTotali();

			// Verifichiamo il punteggio del giocatore
			valutaPunteggio(aGioc);
			/*
			 * Se non ha sballato o non ha totalizzato sette e mezzo,
			 * l'esito del giocatore e' "ok" (ancora in gioco)
			 */
			infoPunteggio += "&esito=" + "ok";
		}
		catch (SballatoException e) 
		{
			// Nel caso il giocatore ha sballato
			infoPunteggio += "&esito=" + "sballato";
		} 
		catch (SetteMezzoException e) 
		{
			// Nel caso il giocatore ha totalizzato sette e mezzo
			infoPunteggio += "&esito=" + "setteMezzo";
		}

		return infoPunteggio;
	}
	
	/*
	 * Valuta il punteggio del giocatore passato come parametro
	 * @param aGioc il giocatore di cui bisogna valutare il punteggio 
	 * @throws SetteMezzoException nel caso il giocatore ha totalizzato sette e mezzo
	 * @throws SballatoException nel caso il giocatore ha sballato
	 */
	private void valutaPunteggio(Giocatore aGioc) 
				throws SballatoException, SetteMezzoException {
		
		if(aGioc.daiPuntiTotali() > MAXPUNTI)
			throw new SballatoException();
		else if(aGioc.daiPuntiTotali() == MAXPUNTI)
			throw new SetteMezzoException();
	}

	/*
	 * Verifica se il giocatore ha una matta e una figura 
	 * @param aGioc Il giocatore su cui verificare se ha matta e figura
	 * @return true se il giocatore possiede matta e figura, false altrimenti
	 */
	private boolean mattaFigura(Giocatore aGioc){
		// Ricavo la posizione della eventuale matta
		int pos = aGioc.haMatta();

		//Se il giocatore ha fatto sette e mezzo reale e ha la matta
		if(aGioc.haReale() && pos != -1)
		{
			int numCarte = aGioc.daiCarteSulTavolo().size();
			/*
			 * La posizione della figura in un array di 2 carte
			 * dipende dalla posizione della matta.
			 * Si ricava sottraendo al numero degli elementi 
			 * la posizione della matta, meno uno.
			 * Il meno uno e' giustificato dal fatto che l'indice degli array 
			 * parte da zero e non da uno.
			 */
			int posFig = numCarte -pos -1;

			//Se ha una figura ritorna true, altrimenti false
			if(aGioc.daiCarteSulTavolo().get(posFig).daiValore() == 0.5)
				return true;
			else 
				return false;
		}

		return false;			
	}

	/*
	 * Consente di salvare nel database le informazioni
	 * sulla mano corrente del giocatore passato come parametro
	 * @param aGioc Il giocatore  di cui bisogna salvare le informazioni
	 * @param email L'email del giocatore
	 */
	private void salvaManoDB(Giocatore aGioc, String email) {
		// Salviamo la mano dell'utente
		if(aGioc instanceof GiocatoreUmano && !email.equalsIgnoreCase("null"))
		{
			if(aGioc.daiRuolo().equalsIgnoreCase(Giocatore.BANCO))
				aGioc.setPuntata(sfidante.daiPuntata());
			
			// Creazione della tupla da inserire nel database 
			PartitaEntry part = new PartitaEntry();
			part.setData(data);
			part.setGiocatore(email);
			part.setRuolo(aGioc.daiRuolo());
			part.setPunteggio(aGioc.daiPuntiTotali());

			if(aGioc.isVincitore())
				part.setVincita("+" + aGioc.daiPuntata());
			else
				part.setVincita("-" + aGioc.daiPuntata());

			try 
			{
				// Salvataggio della tupla nel database
				iPartitaDAO transazione = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
				transazione.addTransazione(part);

				System.out.println("Salvataggio mano db RIUSCITO");
			}
			catch (NullPointerException e) 
			{
				System.err.println("Salvataggio mano db FALLITO");
			}
		}// fine if
	}
}