package settemezzo.interfacciautente;

import java.io.*;
import java.text.*;
import java.util.*;

import settemezzo.interfacciautente.util.*;
import settemezzo.mazzo.*;
import settemezzo.util.database.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Cli</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp L'interfaccia a riga
 * di comando, che consente all'utente di interagire con l'applicazione
 * inviando comandi tramite tastiera e 
 * ricevendo risposte alle elaborazioni tramite testo scritto</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class Cli implements iConsole, Serializable {
	private static final long serialVersionUID = -5110474181405270148L;
	private final static String UMANO = "umano";

	@Override
	public String chiediTesto(String msg){
		stampaMsg(msg);
		return Keyboard.readString();
	}

	@Override
	public int chiediInteger(String msg){
		stampaMsg(msg);
		int num = Keyboard.readInt();
	
		/*
		 * Poiche' Keyboard.readInt() se non riesca a parserizzare
		 * ad intero il valore immesso dall'utente ritorna
		 * il minimo valore previsto per gli interi,
		 * Se ritorna quest'ultimo valore ripete l'operazione di inserimento
		 * intero, altrimenti lo ritorna
		 */
		if(num == Integer.MIN_VALUE)
		{
			stampaErr("Hai inserito valori non previsti!");
			return chiediInteger(msg);
		}
		else
			return num;		
	}

	@Override
	public boolean chiediSiNo(String msg){
		boolean rispBool = false;
		boolean corretto = false;
		char risposta;
		
		do
		{
			stampaMsg(msg);
			
			/*
			 *  Converte il primo carattere della stringa digitata dall'utente
			 *  in minuscolo e lo assegna alla variabile risposta 
			 */
			risposta = Character.toLowerCase(Keyboard.readString().charAt(0));
			
			switch(risposta)
			{
				case 's': 
					rispBool = true; 
					corretto = true; 
					break;
					
				case 'n': 
					rispBool = false; 
					corretto = true;
					break;
				
				default:
					stampaErr("Hai inserito una risposta non prevista");
					break;
			}
			
		}while(!corretto);
		
		return rispBool;
	}

	@Override
	public int videataIniziale(){
		 stampaMsg(
				 	  "\n ==========================================================="
					+ "\n        ______   __  __ ______ ________________ 			 "
					+ "\n       |____  | |  \\/  |  ____|___  /___  / __ \\ 		 "
					+ "\n           / /__| \\  / | |__     / /   / / |  | | 		 "
	                + "\n          / / _ \\ |\\/| |  __|   / /   / /| |  | |  		 "
	                + "\n         / /  __/ |  | | |____ / /__ / /_| |__| | 			 "
	                + "\n        /_/ \\___|_|  |_|______/_____/_____\\____/   		 "
	                + "\n															 "
					+ "\n ==========================================================="
					+ "\n       Autori in ordine alfabetico: Tarantino - Turturo     "
					+ "\n ===========================================================" );
		
		 int intero;
		 boolean corretto = false;
		 
		 do
		 {
			 intero = chiediInteger(
					  "\n   Digita:"
					+ "\n   0. se desideri visionare lo storico"
					+ "\n   1. se desideri giocare" 
					+ "\n   2. se desidere uscire dal gioco");
			 	
			 if(intero < 0 || intero > 3)
				 stampaErr("Hai inserito un numero non corretto!");
			 else
				 corretto = true;
		 }
		 while(!corretto);
			 
		 
		 return intero;
	}

	@Override
	public String chiediData(){
		String data = "\n Digita la data per il quale desideri " +
				"\n visionare lo storico delle partite nel formato GG-MM-AAAA";
		data = chiediTesto(data);
		
		/*
		 * Creazione di un formattatore per la data secondo il pattern 
		 * specificato come argomento nel costruttore
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date day = null;
		try 
		{
			/*
			 * Provo a parserizzare la stringa digitata dall'utente secondo il formattatore
			 */
			day = dateFormat.parse(data);
		} 
		catch (ParseException e) 
		{
			stampaErr("Hai inserito una data non valida!");
			return null;
		}
		
		// Creo un nuovo formattatore...
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		// ...con il quale formattare la data digitata e valida, in formato americano
		data = dateFormat.format(day);
		return data;
	}
	
	@Override
	public void setGiocatore(String nome, String ruolo, int credito, String tipoGioc){
		stampaNome(nome, tipoGioc);
		stampaRuolo(ruolo, tipoGioc);
		stampaCredito(credito, tipoGioc);
	}

	@Override
	public int chiediPuntata(int credito){
		boolean corretto = false;
		int puntata;
		
		// Fintantoche' l'utente non inserisce caratteri corretti
		do{
			// Chiedere il valore di puntata...
			puntata = chiediInteger("\n Quanto desideri puntare per questa mano?" 
						          + "\n ATTENZIONE, inserire un valore compreso tra 1 " 
						          + "e il valore di credito!!!");
			
			/*
			 * ...confrontarlo con il credito e se e' inferiore o uguale ad esso
			 * porre la variabile corretto a true in modo che termini il ciclo,
			 * altrimenti evidenzia l'errore (e quindi riesegue il ciclo perche' corretto = false)
			 */
			if(puntata <= credito)
				corretto = true ;
			else
				stampaErr(" La tua puntata e' superiore al credito! ");
		}while(!corretto);
							
		return puntata;
	}
	
	@Override
	public void stampaMsg(String msg){
		System.out.println("\n " + msg);
	}
	
	@Override
	public void stampaErr(String msg){
		System.out.println("\n --> " + msg + " <--");
	}

	@Override
	public void stampaStorico(ArrayList<PartitaEntry> storico){
		stampaMsg(storico.toString());
	}
	
	@Override
	public void stampaNome(String nome, String tipoGioc){
		if(tipoGioc.equals(UMANO))
			stampaMsg("\n Benvenuto " + nome + "!");
	}
	
	@Override
	public void stampaRuolo(String ruolo, String tipoGioc){
		if (tipoGioc.equals(UMANO))
			stampaMsg("\n Il tuo ruolo e' " + ruolo);
		else
			stampaMsg("\n Il ruolo del tuo avversario e' " + ruolo);
	}

	@Override
	public void stampaCredito(int credito, String tipoGioc){
		String msg;
		
		if(tipoGioc.equals(UMANO))
		{
			msg =   "\n ================================================= " 
				  + "\n   	Il tuo credito e' di : " + credito + " Euro"	   
				  + "\n ================================================= ";
			
		}
		else
		{
			msg =   "\n =========================================================== " 
				+  "\n   	Il credito del tuo avversario e' di : " + credito + " Euro"	   
				+  "\n ============================================================ ";
		}
		
		stampaMsg(msg);
	}
	
	@Override
	public void stampaPuntata(int puntata, String tipoGioc){
		String msg;
		
		if(tipoGioc.equals(UMANO))
		{
			msg =   "\n ================================================= " 
				  + "\n   	La tua puntata e' di : " + puntata + " Euro"	   
				  + "\n ================================================= ";
			
		}
		else
		{
			msg =   "\n =========================================================== " 
				+  "\n   	Il tuo avversario ha puntato : " + puntata + " Euro"	   
				+  "\n ============================================================ ";
		}
		stampaMsg(msg);
	}

	@Override
	public void stampaCarta(String tipoGioc, ArrayList<Carta> carteSulTavolo){
		Carta carta = carteSulTavolo.get(carteSulTavolo.size()-1);
		
		String msg;
		
		String carte = new String();
		
		/*
		 *  Se e' giocatore umano vengono stampate 
		 *  anche le carte precedentemente ottenute
		 */
		if(tipoGioc.equals(UMANO))
		{
			for (Carta carta2 : carteSulTavolo) 
				carte += "\n\t  " + carta2.toString(); 
			
			msg =  "\n ============================================================================ " 
				+  "\n  Hai ricevuto la CARTA: " + carta.toString()
				+  "\n  Carte sul Tavolo: " + carte 
				+  "\n ============================================================================ ";
		}
		else
		{
			for(int i = 1; i<carteSulTavolo.size(); i++) 
				carte += "\n  " + carteSulTavolo.get(i).toString(); 
			
			/*
			 * Se e' giocatore simulato da CPU viene stampata 
			 * la carta scoperta ricevuta da questo
			 */
			if(carteSulTavolo.size()==1)
			{   
				msg =  "\n ============================================================================ " 
					+  "\n  Il tuo avversario ha ricevuto la carta coperta"
					+  "\n ============================================================================ ";
			}
			else
				msg =  "\n ============================================================================ " 
					+  "\n  Il tuo avversario ha ricevuto la CARTA: " + carta.toString()
					+  "\n  Carte sul Tavolo: " + carte 
					+  "\n ============================================================================ ";
		}
		
		stampaMsg(msg);
	}
	
	@Override
	public void stampaPunteggio(double punteggio, String tipoGioc){
		if(tipoGioc.equals(UMANO))
			stampaMsg("\n Il tuo punteggio e': " + punteggio);
		else
			stampaMsg("\n Il punteggio del tuo avversario e': " + punteggio);
	}
	
	@Override
	public void scopriCarta(Carta cartaCop, double punteggio, String tipoGioc){
		if(!tipoGioc.equals(UMANO))
		{
			String msg =   	"\n =========================================================================="
						  + "\n La carta coperta del tuo avversario e': " + cartaCop
		 			      + "\n Il suo punteggio totale e': " + punteggio
		 			      + "\n ==========================================================================";
			
			stampaMsg(msg);
		}
	}
	
	@Override
	public void reset(){
		String msg = "\n La mano e' terminata!" +
					 "\n " + "\n ";
						
		stampaMsg(msg);
	}
	
	@Override
	public File carica() {
		
		// Creazione di un file che rappresenta la directory di lavoro corrente  
	    File dir = new File(System.getProperty("user.dir"));
	    
	    /*
	     *  Creazione di un'array di stringhe con i quali 
	     *  sono denominati i file nella dir corrente.
	     *  L'array conterra' solo stringhe di quei file 
	     *  la cui estensione e' ".dat"
	     */
	    String[] files = dir.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				/*
				 * Viene estratto il nome del file con nome name,
				 * presente nella directory dir.
				 * Se questa Stringa contiene ".dat", indexOf restituisce la posizione
				 * della prima occorrenza della stringa passata, altrimenti -1.
				 * Se dunque il nome del file contiene ".dat", 
				 * il metono restituisce true, false altrimenti
				 */
				String file = new File(name).getName();
				return file.indexOf(".dat") != -1;
			}
	    });
	    
	    /*
	     * Se esistono file con l'estensione specificata nella directory 
	     * corrente li stampa uno sotto l'altro
	     */
	    if (files != null) 
	    {
	    	stampaMsg("\n Attualmente i salvataggi nella directory " + System.getProperty("user.dir") + " sono: \n ");
	    	
	        for (String file: files)
	        	stampaMsg("\t " + file);
	    }

		String nomePartita = chiediTesto("\n Digita il nome del file che desideri caricare: ");
		File partita = new File(System.getProperty("user.dir") +"/" + nomePartita);
		
		if(partita.exists())
			return partita;
		else
		{
			if(chiediSiNo("\n FILE INESISTENTE!!! \n Vuoi caricare un'altro file?"))
				return carica();
			else 
				return null;
		}
	}

	@Override
	public File salva() {
		String nomePartita = chiediTesto("\n Nome del file da salvare: ");
		File partita = new File(System.getProperty("user.dir") + "/" + nomePartita);
		stampaMsg("\n La partita verra' salvata in " + partita.getAbsolutePath());
		
		return partita;
	}
}