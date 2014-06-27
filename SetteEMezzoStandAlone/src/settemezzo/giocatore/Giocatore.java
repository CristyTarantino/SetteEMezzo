package settemezzo.giocatore;

import java.io.*;
import java.util.*;

import settemezzo.mazzo.*;
import settemezzo.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Giocatore</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un Giocatore 
 * per il gioco del Sette e mezzo che tiene traccia di informazioni relative al giocatore</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public abstract class Giocatore implements Serializable{

	private static final long serialVersionUID = 7137662777463029481L;
	
	/**
	 * Il ruolo Banco del Giocatore
	 */
	public final static String BANCO = new String("Banco");
	
	/**
	 * Il ruolo Sfidante del Giocatore
	 */
	public final static String SFIDANTE = new String("Sfidante");
	
	/*
	 * Rappresenta il massimo credito del giocatore
	 */
	protected final static int MAX_CREDITO = 64000;
	
	/*
	 * Rappresenta il minimo punteggio che il giocatore
	 * potrebbe acquisire richiedendo una nuova carta 
	 */
	protected final static double MIN_PUNTEGGIO = 0.5;
	
	/*
	 * Teniamo traccia del punteggio della matta che varia 
	 * in relazione al punteggio delle altre carte in possesso
	 */
	protected transient double punteggioMatta;
	
	protected transient double punteggioCoperto; // punteggio della carta coperta
	protected transient double punteggio; // punteggio totale del giocatore
	protected transient int puntata;
	protected transient boolean vincitore;
	protected String nome;	
	protected String ruolo;
	protected int credito;
	protected ArrayList<Carta> carteSulTavolo;
	
	/**
	 * Crea un nuovo Giocatore per il gioco del Sette e Mezzo
	 * con credito predefinito.
	 */
	public Giocatore(){
		carteSulTavolo = new ArrayList<Carta>();
		credito = MAX_CREDITO;
	}
	
	/**
	 * Imposta il nome del Giocatore
	 * @param nome Il nome da assegnare al Giocatore
	 */
	public void setNome(String nome){
		this.nome = nome;
	}

	/**
	 * Restituisce il nome del Giocatore
	 * @return Il nome del Giocatore
	 */
	public String daiNome(){
		return nome;
	}
	
	/**
	 * Imposta il ruolo del Giocatore
	 * @param ruolo Il ruolo da assegnare al Giocatore: 
	 * 			<code>BANCO</code>, <code>SFIDANTE</code>
	 */
	public void setRuolo(String ruolo){
		this.ruolo = ruolo; 
	}

	/**
	 * Restituisce il ruolo del Giocatore
	 * @return Il ruolo del Giocatore: 
	 * 			<code>BANCO</code> oppure <code>SFIDANTE</code>
	 */
	public String daiRuolo(){
		return ruolo; 
	}

	/**
	 * Imposta il credito del Giocatore
	 * @param credito Il credito da assegnare al Giocatore
	 */
	public void setCredito(int credito){
		this.credito = credito; 
	}
	
	/**
	 * Determina se il Giocatore ha realizzato Sette e Mezzo reale
	 * @return true se ha realizzato Sette e Mezzo Reale, 
	 * 			false altrimenti 
	 */
	public boolean haReale(){
		/*
		 * Se ha 2 carte sul tavolo e ha totalizzato sette e mezzo
		 * allora ha compiuto sette e mezzo Reale
		 */
		return carteSulTavolo.size() == 2 && punteggio == Partita.MAXPUNTI;
	}
	
	/**
	 * Determina se il Giocatore ha totalizzato un punteggio
	 * superiore a sette e mezzo
	 * @return true se ha sballato, false altrimenti
	 */
	public boolean haSballato(){
		return punteggio > Partita.MAXPUNTI;
	}
	
	/**
	 * Ricerca la prima occorenza della Matta, effettuando un test di 
	 * uguaglianza basato sull'utilizzo del methodo equals di Carta
	 * @return La posizione della Matta tra le carte in mano al Giocatore;
	 * 			ritorna -1 se la Matta non e' stata trovata
	 */
	public int haMatta() {
		return carteSulTavolo.indexOf(Partita.daiMatta());
	}

	/**
	 * Imposta il Giocatore come vincitore della mano corrente 
	 */
	public void setVincitore(){
		vincitore = true;
	}
	
	/**
	 * Determina se il Giocatore e' vincitore della mano corrente
	 * @return true se ha vinto, false altrimenti
	 */
	public boolean isVincitore(){
		return vincitore;
	}
	
	/**
	 * Aggiorna le carte in possesso del Giocatore
	 * @param carta La Carta appena estratta dal mazzo per il giocatore
	 */
	public void setCarteSulTavolo(Carta carta){
		/*
		 * Aggiorno il punteggio totale del giocatore
		 * e il punteggio dell'eventuale matta
		 */
		sommaPunti(carta);
		
		// Memorizzo la carta ricevuta
		carteSulTavolo.add(carta);
		
		/*
		 * La prima carta nella struttura sara' sicuramente la carta coperta, 
		 * quindi il punteggioCoperto sara' uguale a quello
		 * della prima carta ricevuta
		 */		
		if( carteSulTavolo.get(0).equals(Partita.daiMatta()) )
			punteggioCoperto = punteggioMatta;
		else 
			punteggioCoperto = carteSulTavolo.get(0).daiValore();
	}

	/**
	 * Restituisce una lista con le carte in mano al Giocatore
	 * @return Le Carte in mano al Giocatore
	 */
	public ArrayList<Carta> daiCarteSulTavolo(){
		return carteSulTavolo;
	}
	
	/**
	 * Resetta tutti i campi relativi al Giocatore
	 */
	public void reset(){
		punteggio = 0.0;
		punteggioCoperto = 0.0;
		puntata = 0;
		carteSulTavolo.clear();
		vincitore = false;
	}
		
	/**
	 * Restituisce il punteggio relativo alla carta coperta
	 * @return Il punteggio della carta coperta
	 */
	public double daiPuntoCoperto(){
		return punteggioCoperto;
	}
	
	/**
	 * Restituisce il punteggio totale delle carte in possesso dal giocatore
	 * @return Il punteggio totale delle carte in mano al giocatore
	 */
	public double daiPuntiTotali(){
		return punteggio;
	}
	
	/**
	 * Restituisce il valore della puntata del Giocatore
	 * @return La puntata del Giocatore per la mano corrente
	 */
	public int daiPuntata(){
		return puntata;
	}

	/**
	 * Restituisce il credito del Giocatore
	 * @return Il credito del Giocatore
	 */
	public int daiCredito(){
		return credito;
	}

	/**
	 * Aggiorna il credito del Giocatore
	 * @param pntDoppia Indica se il giocatore deve ricevere/pagare
	 * 			una posta semplice (false) o doppia (true) 
	 */
	public void aggiornaCredito(boolean pntDoppia){
		suppAggCredito(pntDoppia, puntata);
	}
	
	/**
	 * Aggiorna il credito del Giocatore
	 * @param pntDoppia Indica se il giocatore deve ricevere/pagare
	 * 			una posta semplice (false) o doppia (true) 
	 * @param posta La cifra con la quale deve essere 
	 * 			aggiornato il credito del giocatore
	 */
	public void aggiornaCredito(boolean pntDoppia, int posta){
		suppAggCredito(pntDoppia, posta);
	}
	
	/*
	 * Metodo di supporto all'aggiornamento del credito
	 * @param pntDoppia Indica se il giocatore deve ricevere/pagare
	 * 			una posta semplice (false) o doppia (true) 
	 * @param posta La cifra con la quale deve essere 
	 * 			aggiornato il credito del giocatore
	 */
	private void suppAggCredito(boolean pntDoppia, int posta) {
		// Se il giocatore non ha vinto, cioe' se ha vinto l'avversario...
		if(!vincitore)
		{
			if(pntDoppia) // posta doppia
			{
				//Raddoppia la posta e sottrae questa dal credito
				posta += posta;
				credito -= posta;
			}
			else // posta semplice
				credito -= posta;			
		}
		else// ... altrimenti il giocatore ha vinto
		{
			if(pntDoppia) // posta doppia
			{
				//Raddoppia la posta e sottrae questa dal credito
				posta += posta;
				credito += posta;
			}
			else // posta semplice
				credito += posta;		
		}
	}
	
	/*
	 * Aggiorna i punti totali del giocatore
	 * @param carta La carta con il cui valore bisogna aggiornare il punteggio totale
	 */
	private void sommaPunti(Carta carta){
		/*
		 * Se il Giocatore non ha carte in mano, 
		 * oppure ha delle carte tra le quali non compare la matta...
		 */
		if(carteSulTavolo.size() == 0 || (carteSulTavolo.size() > 0 && haMatta() == -1) )
		{
			/*
			 * ...allora se la carta appena distribuita al Giocatore e' la matta,
			 * calcola il punteggio della matta e aggiorna il punteggio totale.
			 * In caso contrario il Giocatore ha ricevuta una carta normale
			 * quindi il punteggio di quest'ultima sara' sommato al punteggio totale
			 */
			if( carta.equals(Partita.daiMatta()) )
			{
				punteggioMatta = calcolaPunteggioMatta();
				punteggio += punteggioMatta;
			}
			else
				punteggio += carta.daiValore();
		}
		else
		{
			/*
			 * Altrimenti il Giocatore avra' in mano delle carte 
			 * tra le quali compare la matta: 
			 * 	1. viene calcolato il punteggio privato della matta,
			 * 	2. viene aggiunto a quest'ultimo il valore della nuova carta,
			 * 	3. viene determinato il valore che dovra' assumere la matta 
			 * 		e viene sommato al punteggio totale
			 */
			punteggio -= punteggioMatta;
			punteggio += carta.daiValore();
			punteggioMatta = calcolaPunteggioMatta();
			punteggio += punteggioMatta;
		}
			
	}
	
	/*
	 * Determina il punteggio che dovra' assumere la matta
	 * @return Il nuovo punteggio della matta 
	 */
	private double calcolaPunteggioMatta(){
		// Il punteggio della matta e' uguale al punteggio massimo - punteggio totale
		double pntMatta = Partita.MAXPUNTI - punteggio;
		
		/*
		 * Se il punteggio appena ottenuto e' maggiore di 0.5, 
		 * sara' necessario prendere la parte intera in quanto 
		 * la matta puo' assumere valori interi compresi tra 1 e 7.
		 * Altrimenti restituiamo il valore 0.5
		 */
		if(pntMatta > MIN_PUNTEGGIO)
			return (int) pntMatta;
		else
			return MIN_PUNTEGGIO;
	}
	
	/**
	 * Restituisce il punteggio relativo alle carte scoperte
	 * @return Il punteggio totale delle carte scoperte
	 */
	public abstract double daiPuntiScoperti();
	
	/**
	 * Restituisce il tipo del giocatore
	 * @return il tipo del giocatore
	 */
	public abstract String daiTipoGioc();
	
	/**
	 * Chiede al Giocatore se desidera un'altra carta, 
	 * in base al punteggio dell'avversario
	 * @param puntoAvversario Il punteggio visibile totalizzato dall'avversario
	 * @return true se il Giocatore desidera un'altra carta, false altrimenti
	 */
	public abstract boolean altraCarta(double puntoAvversario);
	
	/**
	 * Chiede al Giocatore se desidera effettuare un'altra mano
	 * @return true se il Giocatore desidera continuare a giocare, false altrimenti
	 */
	public abstract boolean altraMano();
	
	/**
	 * Imposta la puntata del Giocatore
	 * @param puntata La puntata del Giocatore
	 */
	public abstract void setPuntata(int puntata);
	
}