package settemezzo.util.exception;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp MazzoTerminatoException</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp La classe crea 
 * un'eccezione usata per gestire il caso in cui il mazzo di carte e' terminato </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class MazzoTerminatoException extends Exception{
	
	private static final long serialVersionUID = 5109544488954098336L;

	/**
	 * Crea una nuova eccezione con <code>null</code> come messaggio di dettaglio
	 */
	public MazzoTerminatoException(){
		super("Mazzo Terminato!!!");
	}
	
	/**
	 * Crea una nuova eccezione con uno specifico come messaggio di dettaglio
	 * @param msg Il messaggio di dettaglio. (Il messaggio di dettaglio viene 
	 * salvato per un successivo recupero da parte del metodo <code> Throwable.getMessage() <code> 
	 */
	public MazzoTerminatoException(String msg){
		super(msg);
	}
}
