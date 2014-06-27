package settemezzo.util.exception;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp SballatoException </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp La classe crea 
 * un'eccezione usata per gestire il caso in cui un giocatore ha totalizzato 
 * un punteggio superiore a Sette e Mezzo ("Ha Sballato") </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class SballatoException extends Exception{
	
	private static final long serialVersionUID = 4901028483488863571L;

	/**
	 * Crea una nuova eccezione con <code>null</code> come messaggio di dettaglio
	 */
	public SballatoException(){
		super();
	}
	
	/**
	 * Crea una nuova eccezione con uno specifico come messaggio di dettaglio
	 * @param msg Il messaggio di dettaglio. (Il messaggio di dettaglio viene 
	 * salvato per un successivo recupero da parte del metodo <code> Throwable.getMessage() <code> 
	 */
	public SballatoException(String msg){
		super(msg);
	}
}