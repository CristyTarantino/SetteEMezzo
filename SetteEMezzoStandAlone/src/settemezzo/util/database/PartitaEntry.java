package settemezzo.util.database;

import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp PartitaEntry</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un record
 * della tabella setteemezzo_standalone.transazione</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class PartitaEntry {
	
	private String giocatore;
	private String ruolo;
	private double punteggio;
	private String vincita;
	private Date data;
	
	/**
	 * Imposta il nome del Giocatore della mano
	 * @param giocatore Il nome del Giocatore
	 */
	public void setGiocatore(String giocatore){
		this.giocatore = giocatore;
	}

	/**
	 * Restituisce il nome del Giocatore della mano
	 * @return Il nome del Giocatore
	 */
	public String daiGiocatore(){
		return giocatore;
	}
	
	/**
	 * Imposta il ruolo del Giocatore della mano
	 * @param ruolo Il ruolo del Giocatore
	 */
	public void setRuolo(String ruolo){
		this.ruolo = ruolo;
	}

	/**
	 * Restituisce il ruolo del Giocatore della mano
	 * @return Il ruolo del Giocatore
	 */
	public String daiRuolo(){
		return ruolo;
	}
	
	/**
	 * Imposta il punteggio conseguito dal Giocatore della mano
	 * @param punteggio Il punteggio totale del Giocatore
	 */
	public void setPunteggio(double punteggio){
		this.punteggio = punteggio;
	}

	/**
	 * Restituisce il punteggio conseguito dal Giocatore della mano
	 * @return Il punteggio totale del Giocatore
	 */
	public double daiPunteggio(){
		return punteggio;
	}
	
	/**
	 * Imposta la vincita del Giocatore della mano
	 * @param vincita La vincita del Giocatore
	 */
	public void setVincita(String vincita){
		this.vincita = vincita;
	}

	/**
	 * Restituisce la vincita conseguita dal Giocatore della mano,
	 * costituita da un segno (+/-) e da un valore numerico 
	 * che rappresenta la puntata del Giocatore.
	 * Il segno + indica che il giocatore ha vinto la mano,
	 * invec il segno - indica che il giocatore ha perso
	 * @return La vincita del Giocatore
	 */
	public String daiVincita(){
		return vincita;
	}
	
	/**
	 * Imposta la data in cui e' stata giocata la mano di una partita
	 * @param data La data della mano
	 */
	public void setData(Date data){
		this.data = data;
	}

	/**
	 * Restituisce la data in cui e' stata giocata la mano di una partita
	 * @return La data in cui si e' svolta la mano
	 */
	public Date daiData(){
		return data;
	}
		
	/**
	 * Converte in stringa le informazioni relative ad un record della base di dati
	 * @return Stringa con le informazioni sulla mano di una partita
	 */
	public String toString(){
		return "\n Giocatore: "+giocatore
			+"  ruolo: "+ ruolo
			+"  punteggio: "+ punteggio
			+"  vincita: "+ vincita
			+"  data: "+ data;
	}
	
	/**
	 * Converte in un array le informazioni relative ad un record della base di dati
	 * @return Array contenente il nome del giocatore, il ruolo, 
	 * 			il punteggio conseguito, la vincita ottenuta 
	 * 			e la data in cui si e' svolta la partita
	 */
	public Object[] toArray(){
		return new Object[]{giocatore, ruolo, punteggio, vincita, data};
	}
}