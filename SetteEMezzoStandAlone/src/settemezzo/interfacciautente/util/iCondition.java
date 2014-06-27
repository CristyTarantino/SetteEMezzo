package settemezzo.interfacciautente.util;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iCondition </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Interface description:</b><br /> &nbsp &nbsp &nbsp &nbsp Le istanze 
 * delle classi che implementano questa interfaccia, rappresentano delle condizioni
 * in base alle quali eseguire dei controlli specifici</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public interface iCondition {
	
	/**
	 * Valida la stringa in input. 
	 * @param input la stringa da validare.
	 * @return true se la validazione e' andata a buon fine,
	 * 		   false altrimenti.
	 */
	public boolean check(String input);
	
	/**
	 * Ritorna il tipo di errore riscontrato in una validazione errata.
	 * @return L'errore riscontrato.
	 */
	public String getKindError();	
}
