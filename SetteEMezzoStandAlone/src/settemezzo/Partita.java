package settemezzo;

import java.io.*;
import java.util.*;

import settemezzo.giocatore.*;
import settemezzo.interfacciautente.*;
import settemezzo.mazzo.*;
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
	
	private static Carta MATTA;
	
	private Mazzo mazzoCarte;
	private Giocatore sfidante;
	private Giocatore banco;
	private Date data;
	private iConsole video;
	private boolean mattaEstratta;
	private boolean swap;
	
	/**
	 * Inizializza una nuova partita a Sette e Mezzo.
	 * Inizializza il mazzo da utilizzare, l'interfaccia utente,
	 * i due giocatori (banco e sfidante)
	 * @param interfaccia L'interfaccia utente da utilizzare
	 * @param mazzo Il mazzo di carte con il quale giocare la partita
	 * @param matta La Carta che assume il valore di matta nel mazzo passato
	 */
	public Partita(iConsole interfaccia, Mazzo mazzo, Carta matta){
		MATTA = matta;
		video = interfaccia;
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
	public static Carta daiMatta(){
		return MATTA;
	}
	
	/**
	 * Avvia la partita
	 */
	public void avvia(){
		
		// Presentiamo all'utente una schermata iniziale di scelta
		int opzione = video.videataIniziale();
		
		switch(opzione)
		{
			// Richiesta storico partite
			case 0: 
				// Richiesta della data all'utente
				String data = video.chiediData();
				
				// Se ha inserito una data
				if(data != null)
				{	
					try 
					{
						iPartitaDAO transazione = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
						ArrayList<PartitaEntry> storico = transazione.getTransazioni(data);
						video.stampaStorico(storico);
					}
					catch (NullPointerException e) 
					{
						video.stampaErr("Impossibile interrogare il database!!!");
					}
					
				}
				avvia();
				break;
			
			// Avvio del gioco
			case 1: gioco(); break;
			
			// Uscita dal gioco
			default: System.exit(0); break;
		}
		
	}
	
	/*
	 * Contiene la logica di esecuzione di una nuova partita,
	 * gestisce il caricamento ed il salvataggio della stessa 
	 */
	private void gioco(){
		boolean risposta = false;
		
		// Richiesta caricamento partita
		if(video.chiediSiNo("\n Vuoi caricare una partita?"))
			risposta = carica();
			
		// Se il caricamento della partita e' andato a buon fine ...
		if(risposta)
		{
			// ... reimposta l'interfaccia con i dati dei giocatori
			video.setGiocatore(banco.daiNome(), banco.daiRuolo(), 
					banco.daiCredito(), banco.daiTipoGioc());
			
			video.setGiocatore(sfidante.daiNome(), sfidante.daiRuolo(), 
					sfidante.daiCredito(), sfidante.daiTipoGioc());
		}
		else
		{
			sfidante.setNome(video.chiediTesto("\n Inserisci il tuo nome: "));
			video.stampaNome(sfidante.daiNome(), sfidante.daiTipoGioc());
			video.stampaNome(banco.daiNome(), banco.daiTipoGioc());
			
			video.stampaRuolo(sfidante.daiRuolo(), sfidante.daiTipoGioc());
			video.stampaRuolo(banco.daiRuolo(), banco.daiTipoGioc());
			
			video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
			video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
			mazzoCarte.mescola();
		}
			
		/*
		 * La partita continua fintantoche' lo sfidante
		 * e il banco hanno credito e lo sfidante desidera giocare 
		 */
		do
		{
			// Reset dell'interfaccia
			video.reset();
			
			if(mattaEstratta)
			{	
				mazzoCarte.mescola();
				mattaEstratta = false;
				video.stampaMsg("\n Mazzo Mescolato!!! E' stata estratta la Matta!!! ");
			}
			
			// Distribuzione della prima carta, ossia della carta coperta
			distribuisciCarta(sfidante);
			distribuisciCarta(banco);
			
			int pntTemp = 0;
			
			if(sfidante instanceof GiocatoreUmano)
				pntTemp = video.chiediPuntata(sfidante.daiCredito());
				
			sfidante.setPuntata(pntTemp);
			video.stampaPuntata(sfidante.daiPuntata(), sfidante.daiTipoGioc());
			
			try 
			{
				// Gioca lo sfidante
				mano(sfidante, banco.daiPuntiScoperti());
			} 
			catch (SetteMezzoException e) 
			{
				video.stampaMsg(e.getMessage());
			} 
			catch (SballatoException e) 
			{
				video.stampaErr(e.getMessage());
			}
			
			try 
			{
				// Il banco gioca solo se lo sfidante non ha sballato
				if(!sfidante.haSballato())
					mano(banco, sfidante.daiPuntiScoperti());
			}
			catch (SetteMezzoException e) 
			{
				video.stampaMsg(e.getMessage());
			}
			catch (SballatoException e) 
			{
				video.stampaErr(e.getMessage());
			}
			
			// Mostro le carte coperte dei giocatori
			video.scopriCarta(sfidante.daiCarteSulTavolo().get(0), sfidante.daiPuntiTotali(), sfidante.daiTipoGioc());
			video.scopriCarta(banco.daiCarteSulTavolo().get(0), banco.daiPuntiTotali(), banco.daiTipoGioc());
						
			valutaVincitore();
			
			try
			{
				// Salvataggio della mano nel database
				salvaManoDB(banco);
				salvaManoDB(sfidante);
			}
			catch(NullPointerException e)
			{
				video.stampaErr("La mano non e' stata salvata!");
			}
			finally
			{
				// Verifica la necessita' di effettuare lo swap dei giocatori
				scambiaRuoli();
					
				// Reset di alcuni campi dei giocatori della partita
				banco.reset();
				sfidante.reset();
				this.swap = false;
			}
			
			if(sfidante instanceof GiocatoreUmano)
				((GiocatoreUmano) sfidante).setAltraMano(video.chiediSiNo("\n Vuoi giocare un'altra mano?"));
		
		} 
		while( banco.daiCredito() > 0 && 
				sfidante.daiCredito() > 0 && sfidante.altraMano() );

		/*
		 * Se lo sfidante ha deciso di terminare la partita
		 * e il credito dei giocatori non si e' esaurito ...
		 */
		if(banco.daiCredito() > 0 && sfidante.daiCredito() > 0)
		{
			// ... permetto il salvataggio della stessa
			if(video.chiediSiNo("\n Vuoi salvare la partita? "))
				salva();
			else 
				video.stampaMsg("\n La partita e' terminata!!! ");
		}
		else // Uno dei due gocatori ha esaurito il credito
		{
			// Visualizzo messaggio e non permetto il salvataggio
			String nome = "";
			
			if(banco.daiCredito() <= 0)
				nome = banco.daiNome();
			else
				nome = sfidante.daiNome();
				
			video.stampaErr("Il credito di " + nome	+ " e' inferiore a zero!");
			video.stampaMsg("\n La tua partita termina qui!!! ");
		}
	}

	/*
	 * Effettua lo scambio dei ruoli dei giocatori
	 */
	private void scambiaRuoli() {
		int credito;
		
		// Se e' necessario eseguire lo swap ...
		if(swap)
		{
			// ... e se il giocatore umano dovra' essere banco
			if(sfidante instanceof GiocatoreUmano)
			{
				// viene salvato il credito del giocatore cpu...
				credito = banco.daiCredito();
				banco = sfidante;
				
				// imposto il ruolo del GiocatoreUmano e lo visualizzo
				banco.setRuolo(Giocatore.BANCO);
				video.stampaRuolo(banco.daiRuolo(), banco.daiTipoGioc());
				
				
				sfidante = new SfidanteCPU();
				
				// ... ed assegnato alla nuova istanza che rappresenta la cpu
				sfidante.setCredito(credito);
				
				video.stampaRuolo(sfidante.daiRuolo(), sfidante.daiTipoGioc());
				
				video.stampaMsg("\n Ora sei diventato Banco!!!");
			}
			else
			{
				credito = sfidante.daiCredito();
				sfidante = banco;
				
				// imposto il ruolo del GiocatoreUmano e lo visualizzo
				sfidante.setRuolo(Giocatore.SFIDANTE);
				video.stampaRuolo(sfidante.daiRuolo(), sfidante.daiTipoGioc());
				
				
				banco = new BancoCPU();
				banco.setCredito(credito);
				
				video.stampaRuolo(banco.daiRuolo(), banco.daiTipoGioc());
				
				video.stampaMsg("\n Ora sei diventato Sfidante!!!");
			}
		}
	}
	
	/*
	 * Permette il salvataggio di una partita
	 */
	private void salva(){		
		File partita = video.salva();
		
		// Se e' stato selezionato un file valido
		if(partita != null)
		{
			try 
			{	
				// creo l'oggetto file di partita
				FileOutputStream partitaFileOut = new FileOutputStream(partita.getPath() + ".dat");
				
				// istanzio un oggetto dalla classe ObjectOutputStream a cui passo l'oggetto file di partita, questo perchè ObjectOutputStream è un oggetto per manipolare i file
				ObjectOutputStream partitaSer = new ObjectOutputStream(partitaFileOut);
				
				// serializzo i dati nello stream
				partitaSer.writeObject(sfidante);
				partitaSer.writeObject(banco);
				partitaSer.writeObject(mazzoCarte);
				partitaSer.writeObject(MATTA);
				partitaSer.writeObject(mattaEstratta);
								
				// attivo il flusso di dati verso il file contenuto in serPartita
				partitaSer.flush();

				// chiudo il file
				partitaFileOut.close();
				
				video.stampaMsg("\n La partita e' stata salvata con successo!!! ");
			} 
			catch (FileNotFoundException e) 
			{
				video.stampaErr("Nome File Non Valido");
			} 
			catch (IOException e) 
			{
				video.stampaErr("Errore di Salvataggio");
			}
		}		
	}
	
	/*
	 * Permette il caricamento di una partita precedentemente salvata
	 * @return true se il caricamento e' andato a buon fine, 
	 * 			false se sono stati riscontrati errori 
	 * 			o se l'utente ha annulato il caricamento
	 */
	private boolean carica(){
		
		File partita = video.carica();
		
		// Se l'utente ha selezionato un file valido
		if(partita != null)
		{
			try
			{
				// creo l'oggetto file di partita
				FileInputStream partitaFileIn = new FileInputStream(partita);
				
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
	
				// chiudo il file
				partitaFileIn.close();
				
				return true;
			}
			catch(FileNotFoundException e)
			{
				video.stampaErr("Nome File Non Valido");
				return false;
			}		
			catch(IOException e)
			{
				video.stampaErr("Errore di Caricamento");
				return false;
			}
			catch(ClassNotFoundException e)
			{
				video.stampaErr("Errore di Serializzazione");
				return false;
			}
		}
		else
			return false;		
	}
	
	/*
	 * Gestisce la mano di un giocatore umano.
	 * @param aGioc il giocatore in esame 
	 * @param puntoAvversario il punteggio dell'avversario
	 */
	private void mano(Giocatore aGioc, double puntoAvversario) 
					throws SetteMezzoException, SballatoException {
		/*
		 * Serve per terminare il ciclo nel caso in cui
		 * il giocatore ha sballato o ha totalizzato sette e mezzo;
		 * Soluzione equilavente e' l'adozione del break nel catch 
		 * dopo la stampa del messaggio d'errore
		 */
		boolean altramano = false;
		
		/*
		 * Fintantoche' il giocatore richiede un'altra carta
		 * e non ha sballato ne ha totalizzato sette e mezzo
		 */
		while(!altramano)
		{
			if(aGioc instanceof GiocatoreUmano)
			{
				boolean daiCarta = video.chiediSiNo("\n Desideri un'altra carta?");
				((GiocatoreUmano) aGioc).setAltraCarta(daiCarta);
			}
			
			if(aGioc.altraCarta(puntoAvversario))
			{
				//Distribuisce una carta
				distribuisciCarta(aGioc);
				
			    /*
			     * Se il giocatore ha sballato o ha totalizzato sette e mezzo,
			     * viene solleva l'eccezione relativa
			     */
				if(aGioc.daiPuntiTotali() > MAXPUNTI)
					throw new SballatoException(aGioc.daiNome() + " ha SBALLATO!!!");
				else if(aGioc.daiPuntiTotali() == MAXPUNTI)
					throw new SetteMezzoException(aGioc.daiNome() 
													+ " ha fatto SETTE E MEZZO!!!");
			}
			else
				altramano = true;
		}			
	}
	
	/*
	 * Distribuisce una carta al giocatore aggiornando le sue carte sul tavolo 
	 * @param aGioc Giocatore a cui viene distribuita la carta
	 */
	private void distribuisciCarta(Giocatore aGioc) {
		try
		{
			// Si estrae una carta dal mazzo
			Carta cartaDis = mazzoCarte.carta();

			// Si controlla se e' la matta
			if( cartaDis.equals(MATTA) )
				mattaEstratta = true;
			
			// Si consegna la carta al giocatore che quindi la disporra' in carteSulTavolo
			aGioc.setCarteSulTavolo(cartaDis);
			
			// Visualizza al relativo giocatore la carta estratta
			video.stampaCarta(aGioc.daiTipoGioc(), aGioc.daiCarteSulTavolo());
			
			if(aGioc instanceof GiocatoreUmano)
				video.stampaPunteggio(aGioc.daiPuntiTotali(), aGioc.daiTipoGioc());
			else
				video.stampaPunteggio(aGioc.daiPuntiScoperti(), aGioc.daiTipoGioc());
			
		}
		catch(MazzoTerminatoException e)
		{
			video.stampaErr(e.getMessage() + "\n Il mazzo verra' ora mescolato!");
			
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
	 * Valuta il vincitore tra i giocatori, aggiornando il credito
	 * dello sfidante e del banco in base ai criteri di gioco
	 */
	private void valutaVincitore() {
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
				video.stampaMsg(bancoSemplice);
			}
			else //altrimenti posta doppia
			{
				sfidante.aggiornaCredito(pntDoppia);
				banco.aggiornaCredito(pntDoppia, sfidante.daiPuntata());
				video.stampaMsg(bancoDoppio);
			}
			
			video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
			video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
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
				video.stampaMsg(sfidanteDoppio);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				
				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
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
				video.stampaMsg(bancoSemplice);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
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
				video.stampaMsg(sfidanteDoppio);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				
				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
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
				video.stampaMsg(sfidanteSemplice);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				
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
					video.stampaMsg(bancoSemplice);
					video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
					video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				}
				else
				{
					sfidante.setVincitore();
					sfidante.aggiornaCredito(!pntDoppia);
					banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
					video.stampaMsg(sfidanteSemplice);
					video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
					video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
					
				}
			}
			else // se lo sfidante ha sballato gli viene sottratta una posta semplice
			{
				banco.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
				video.stampaMsg(bancoSemplice);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
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
				video.stampaMsg(sfidanteDoppio);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				
				// Bisogna effettuare lo scambio dei ruoli
				swap = true;
			}
			else
			{
				sfidante.setVincitore();
				sfidante.aggiornaCredito(!pntDoppia);
				banco.aggiornaCredito(!pntDoppia, sfidante.daiPuntata());
				video.stampaMsg(sfidanteSemplice);
				video.stampaCredito(sfidante.daiCredito(), sfidante.daiTipoGioc());
				video.stampaCredito(banco.daiCredito(), banco.daiTipoGioc());
				
			}
		}
	}
		
	/*
	 * Verifica se il giocatore ha una matta e una figura 
	 * @param aGioc Il giocatore su cui verificare se ha matta e figura
	 * @return true se il giocatore ha matta e figura, false altrimenti
	 */
	private boolean mattaFigura(Giocatore aGioc) {
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
	 */
	private void salvaManoDB(Giocatore aGioc) {
		// Salviamo la mano dell'utente
		if(aGioc instanceof GiocatoreUmano)
		{
			if(aGioc.daiRuolo().equalsIgnoreCase(Giocatore.BANCO))
				aGioc.setPuntata(sfidante.daiPuntata());
			
			// Creazione della tupla da inserire nel database 
			PartitaEntry part = new PartitaEntry();
			part.setData(data);
			part.setGiocatore(aGioc.daiNome());
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
			}
			catch (NullPointerException e) 
			{
				video.stampaErr("Impossibile interrogare il database!!!");
			}
		}// fine if
	}
}