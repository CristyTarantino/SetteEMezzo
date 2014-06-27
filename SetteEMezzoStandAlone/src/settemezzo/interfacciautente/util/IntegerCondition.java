package settemezzo.interfacciautente.util;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp IntegerCondition </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp La classe rappresenta
 * la condizione in base al quale viene verificato se un testo e' identificabile come
 * numero intero </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class IntegerCondition implements iCondition{
	
	@Override
	public boolean check(String input){
		try{
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}
	
	@Override
	public String getKindError(){
		return "Hai inserito caratteri non numerici!";
	}
}
