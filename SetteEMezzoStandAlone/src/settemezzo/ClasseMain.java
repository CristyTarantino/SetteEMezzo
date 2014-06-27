package settemezzo;

import java.awt.Color;
import java.io.*;
import java.net.*;
import javax.swing.*;

import settemezzo.interfacciautente.*;
import settemezzo.mazzo.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp ClasseMain</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che mostra
 * un set di opzioni che consente all'utente di effettuare una scelta 
 * in base alla quale avviare una partita a Sette e Mezzo </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class ClasseMain {
	
	/**
	 * Crea un pannello con un set di opzioni 
	 * che consente all'utente di effettuare una scelta 
	 * in base alla quale avviare una partita a Sette e Mezzo
	 * @param args parametri di avvio (non previsti)
	 */
	public static void main(String[] args){
		try
        {
            /*
             * Creazione di una ServerSocket e assegnazione di una porta
             * Cio' assicura che si possa avviare una sola partita su ciascuna
             * macchina.
             * Infatti, se la porta e' gia' in uso dal sistema, non potra'
             * essere creata una nuova socket su quella porta, dunque
             * verra' sollevata l'eccezione relativa a tale problema,
             * e verra' interrotto l'esecuzione delle istruzioni nel blocco try
             */
            new ServerSocket(3000);
            
    		iConsole console = null;
    		
    		// In base alla decisione dell'utente
    		switch(dialog())
    		{
    		 case 0: // Viene creata una Gui
    			 console = new Gui();
    		   break;
    		 case 1: // Piuttosto una Console Testuale
    			 console = new Cli();
    		   break;
    		 default : // Piuttosto che uscire completamente dal gioco
    			 System.exit(0);
    		   break;
    		}

    		/*
    		 * Viene creata una partita passando al costruttore di questa
    		 * la console con il quale l'utente ha scelto di giocare,
    		 * il mazzo e la carta rappresentante la matta per quel mazzo.
    		 * Infine, si avvia la partita
    		 */
    		Partita p1 = new Partita(console, new MazzoNapoletano(), MazzoNapoletano.MATTA);
    		p1.avvia();

        }
        catch (IOException e)
        {
        	/*
        	 * Se verra' sollevata l'eccezione apparira' un messaggio informativo
        	 * riguardante l'esistenza in esecuzione della nostra applicazione
        	 */
        	JLabel msglab = new JLabel("<html><b>Applicazione gia' " +
        			"in esecuzione!</b></html>", SwingConstants.CENTER);
    		msglab.setForeground(Color.red);
            JOptionPane.showMessageDialog(null, msglab, 
            		"Errore di Esecuzione", JOptionPane.ERROR_MESSAGE);
        }
        finally
        {
        	// Infine, in entrambi i casi, l'applicazione deve essere arrestata
        	System.exit(0);
        }
    }

	private static int dialog(){
		/*
		 * Creazione di un array di oggetti indicanti 
		 * le scelte che un utente puo' effettuare
		 */
		Object[] opzioni = {"Modalita' Grafica", "Modalita' Testuale", 
									"Esci dal Gioco"};
		
		/*
		 * Quando l'utente clicchera' su uno dei bottoni 
		 * presenti nella finestra iniziale,
		 * la variabile decisione assumera' il valore dell'indice, che nell'array
		 * options, che punta all' opzione (botttone) scelto) 
		 */
		int decisione = JOptionPane.showOptionDialog(null,
				"Cliccare il pulsante relativo alla modalita' di gioco desiderata!",
				"Modalita' di Gioco",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opzioni,
				opzioni[0]); // La scelta di default e' quella posta in prima posizione
							 //   nell' array options
		
		return decisione;
	}
}