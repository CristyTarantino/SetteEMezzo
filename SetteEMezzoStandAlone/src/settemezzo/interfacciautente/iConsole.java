package settemezzo.interfacciautente;

import java.io.*;
import java.util.*;

import settemezzo.mazzo.*;
import settemezzo.util.database.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iConsole</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Interface description:</b><br /> &nbsp &nbsp &nbsp &nbsp Le istanze 
 * delle classi che implementano questa interfaccia, 
 * sono usate come interfaccia utente per il gioco del Sette e Mezzo</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public interface iConsole{
	
	/**
	 * Visualizza un messaggio 
	 * @param msg Il messaggio da visualizzare
	 */
	public void stampaMsg(String msg);
	
	/**
	 * Visualizza un messaggio di errore 
	 * @param msg Il messaggio d'errore da visualizzare
	 */
	public void stampaErr(String msg);
	
	/**
	 * Visualizza una domanda e richiede per essa
	 * una risposta positiva o negativa
	 * @param msg La domanda da visualizzare
	 * @return true se la risposta e' positiva, false altrimenti
	 */
	public boolean chiediSiNo(String msg);
	
	/**
	 * Visualizza una domanda e richiede per essa
	 * una risposta di tipo intero
	 * @param msg La domanda da visualizzare
	 * @return Il numero intero digitato dall'utente
	 */
	public int chiediInteger(String msg);
	
	/**
	 * Visualizza una domanda e richiede per essa
	 * una risposta di tipo testuale
	 * @param msg La domanda da visualizzare
	 * @return La stringa digitata dall'utente
	 */
	public String chiediTesto(String msg);
	
	/**
	 * Richiede una puntata in relazione al credito del giocatore
	 * @param credito il credito del giocatore in esame
	 * @return La puntata del giocatore
	 */
	public int chiediPuntata(int credito);
	
	/**
	 * Visualizza la carta passata come parametro al giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 * @param carteSulTavolo Le carte sul tavolo del giocatore in esame
	 */
	public void stampaCarta(String tipoGioc, ArrayList<Carta> carteSulTavolo);
	
	/**
	 * Visualizza la puntata del giocatore in esame
	 * @param puntata La puntata da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void stampaPuntata(int puntata, String tipoGioc);
	
	/**
	 * Visualizza il credito del giocatore in esame
	 * @param credito Il credito da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void stampaCredito(int credito, String tipoGioc);
	
	/**
	 * Scopre la carta coperta del giocatore in esame
	 * @param cartaCop La carta coperta del giocatore in esame
	 * @param punteggio Il punteggio del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void scopriCarta(Carta cartaCop, double punteggio, String tipoGioc);
	
	/**
	 * Permette di visualizzare la videata iniziale del gioco.
	 * L'utente sceglierà una opzione dal menu' presentatogli
	 * @return La scelta dell'utente
	 */
	public int videataIniziale();
	
	/**
	 * Resetta l'interfaccia utente
	 */
	public void reset();
	
	/**
	 * Visualizza il nome del giocatore in esame
	 * @param nome Il nome del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void stampaNome(String nome, String tipoGioc);
	
	/**
	 * Visualizza il ruolo del giocatore in esame
	 * @param ruolo Il ruolo del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void stampaRuolo(String ruolo, String tipoGioc);
	
	/**
	 * Visualizza il punteggio del giocatore in esame
	 * @param punteggio Il punteggio del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void stampaPunteggio(double punteggio, String tipoGioc);
	
	/**
	 * Reimposta l'interfaccia utente con i dati del giocatore
	 * @param nome Il nome del giocatore in esame
	 * @param ruolo Il ruolo del giocatore in esame
	 * @param credito Il credito del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame
	 */
	public void setGiocatore(String nome, String ruolo, int credito, String tipoGioc);
	
	/**
	 * Presenta un calendario modale per la richiesta di una data
	 * @return la data scelta dall'utente nel formato "yyyy-MM-dd"
	 */
	public String chiediData();
	
	/**
	 * Visualizza l'elenco delle partite salvate
	 * @param storico Lista di partite salvate
	 */
	public void stampaStorico(ArrayList<PartitaEntry> storico);
	
	/**
	 * Richiede il nome del file in cui salvare la partita 
	 * @return Il file in cui salvare i dati relativi alla partita
	 */
	public File salva();
	
	/**
	 * Richiede il nome del file da cui caricare la partita 
	 * @return Il file da cui caricare i dati relativi alla partita
	 */
	public File carica();
}