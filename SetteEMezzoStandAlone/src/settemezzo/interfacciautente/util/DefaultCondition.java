package settemezzo.interfacciautente.util;

import java.util.regex.Pattern;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp DefaultCondition </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp La classe rappresenta
 * la condizione in base al quale viene verificato se un testo e' identificabile come
 * un serie di caratteri non nulli </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class DefaultCondition implements iCondition{

	@Override
	public boolean check(String input){
		if(input.equals("") || input == null || Pattern.matches("(\\s)*", input))
			return false;
		else
			return true;
	}
	
	@Override
	public String getKindError(){
		return "Hai inserito caratteri non validi!";
	}
}
