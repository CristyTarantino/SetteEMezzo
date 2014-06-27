package settemezzo.interfacciautente.util;

import java.io.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Keyboard </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Facilita l'input da tastiera
 * creando una astrazione sui dettagli riguardante il parsing, la conversione 
 * e la cattura dele eccezioni</p>
 * @author Lewis - Loftus
 */
public class Keyboard {
	private static boolean printErrors = true;
	private static int errorCount = 0;

	// ************* Error Handling Section **************************
	
	/**
	 * Ritorna il numero degli errori individuati
	 * @return Il numero di errori individuati 
	 */
	public static int getErrorCount(){
		return errorCount;
	}

	
	/**
	 * Resetta il numero degli errori
	 */
	public static void resetErrorCount() {
		errorCount = 0;
	}

	
	/**
	 * Indica se gli errori in input
	 * sono stati stampati sullo standard output
	 * @return true se gli errori sono stati visualizzati,
	 * 			false altrimenti
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}

	
	/** 
	 * Indicare se gli errori di input
	 * devono essere stampati sullo standard output
	 * @param flag true se devono essere visualizzati, 
	 * 			false altrimenti
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	
	/*
	 * Incrementa il contatore degli errori e
	 * stampa a video il messaggio di errore se desiderato
	 * @param str Il messaggio di errore
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************

	private static String current_token = null;
	// StringTokenizer divide la stringa in token.
	private static StringTokenizer reader;
	// BufferedReader legge il testo da un flusso di input (tastiera nel nostro caso)
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * Ottiene il prossimo token di input ammettendo 
	 * che ci siano linee di input successive
	 * @return Il prossimo token
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}

	/*
	 * Ottiene il successivo token, il quale potrebbe esser stato gia' letto.
	 * @param skip true se le successive linee sono state utilizzate,
	 * 			false altrimenti
	 * @return il successivo token
	 */
	private static String getNextToken(boolean skip) {
		String token;
		
		// se non e' stato letto nessun token, leggilo
		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			// un token e' stato letto ora tocca restituirlo
			token = current_token;
			current_token = null;
		}

		return token;
	}

	/**
	 * Ottiene il prossimo token di input, il quale potrebbe venir
	 * dalla linea di input corrente o da una successiva linea di input.
	 * @param skip true se le linee successive sono state usate,
	 * 			false altrimenti
	 * @return il prossimo token, oppure <code>null</code> in caso di errore
	 */
	private static String getNextInputToken(boolean skip) {
		/* servono per capire quando un token e' finito e ne inizia un'altro
		 * \t carattere tab \n carattere new line 
		 * \r carattere di carriage return \f carattere di formfeed
		 */
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			// se la stringa non e' stata tokenizzata, tokenizzala ora
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);
			
			//?? ciclo per leggere le successive linee di input ??
			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Determina la fine della linea d'input corrente
	 * @return true se non ci sono piu' token da leggere, 
	 * 			false altrimenti
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	// ************* Reading Section *********************************

	/**
	 * Restituisce una stringa letta dallo standard input
	 * @return la stringa letta dallo standard input 
	 * 			oppure <code>null</code> in caso di errori
	 */
	public static String readString() {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
		return str;
	}

	/**
	 * Restituisce una sottostringa delimitata da spazi 
	 * (la prima parola della stringa) letta dallo standard input
	 * @return la prima parola della stringa, oppure <code>null</code>
	 * 			in caso di errori
	 */
	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			token = null;
		}
		return token;
	}

	/**
	 * Restituisce un booleano letto dallo standard input
	 * @return <code>true</code> oppure <code>false</code>.
	 * In caso di errori <code>false</code>
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Error reading boolean data, false value returned.");
				bool = false;
			}
		} catch (Exception exception) {
			error("Error reading boolean data, false value returned.");
			bool = false;
		}
		return bool;
	}

	/**
	 * Restituisce un carattere letto dallo standard input
	 * @return il carattere letto dallo standard input,
	 * 			<code>Character.MIN_VALUE</code> in caso di errori
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Restituisce un intero letto dallo standard input
	 * @return l'intero letto dallo standard input,
	 * 			<code>Integer.MIN_VALUE</code> in caso di errori
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Restituisce un long letto dallo standard input
	 * @return il long letto dallo standard input,
	 * 			<code>Long.MIN_VALUE</code> in caso di errori
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Error reading long data, MIN_VALUE value returned.");
			value = Long.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Restituisce un float letto dallo standard input
	 * @return il float letto dallo standard input,
	 * 			<code>Float.NaN</code> in caso di errori
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			value = (new Float(token)).floatValue();
		} catch (Exception exception) {
			error("Error reading float data, NaN value returned.");
			value = Float.NaN;
		}
		return value;
	}

	/**
	 * Restituisce un double letto dallo standard input
	 * @return il double letto dallo standard input,
	 * 			<code>Double.NaN</code> in caso di errori
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			value = (new Double(token)).doubleValue();
		} catch (Exception exception) {
			error("Error reading double data, NaN value returned.");
			value = Double.NaN;
		}
		return value;
	}
}