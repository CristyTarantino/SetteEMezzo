package settemezzo.util.exception;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp SetteMezzoException </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp La classe crea 
 * un'eccezione usata per gestire il caso in cui un giocatore ha realizzato 
 * un punteggio pari a Sette e Mezzo </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class SetteMezzoException extends Exception {

	private static final long serialVersionUID = -2008216555540433668L;
	
	/**
	 * Crea una nuova eccezione con <code>null</code> come messaggio di dettaglio
	 */
	public SetteMezzoException(){
		super("Hai fatto Sette e Mezzo!!!");
	}
	
	/**
	 * Crea una nuova eccezione con uno specifico come messaggio di dettaglio
	 * @param msg Il messaggio di dettaglio. (Il messaggio di dettaglio viene 
	 * salvato per un successivo recupero da parte del metodo <code> Throwable.getMessage() <code> 
	 */
	public SetteMezzoException(String msg){
		super(msg);
	}
	
}