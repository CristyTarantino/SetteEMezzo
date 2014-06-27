package settemezzo.mazzo;

import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp MazzoNapoletano</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un mazzo di carte 
 * napoletane, opportunamente manipolabile per il gioco del Sette e Mezzo </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class MazzoNapoletano extends Mazzo {
	
	private static final long serialVersionUID = -2446609498466783003L;
	
	// Utilizzata per la costruzione dell'array nella superclasse
	private static final int NUMERO_CARTE = 40;
	
	/*
	 * HashMap che ci permette di associare per ogni chiave TipoCarta, 
	 * il relativo valore previsto per il gioco del Sette e Mezzo
	 */
	private HashMap<TipoCarta, Double> valore = new HashMap<TipoCarta, Double>();
	
	/**
	 * La carta Matta per il gioco del Sette e Mezzo con carte napoletane
	 */
	public final static Carta MATTA = new Carta(Seme.Denari, TipoCarta.Re, 0.5);
	
	/**
	 * Crea un mazzo di carte napoletane per il gioco del Sette e Mezzo
	 */
	public MazzoNapoletano(){
		//Chiamata al costruttore della superclasse per costruire un mazzo di 40 carte
		super(NUMERO_CARTE);		
		associaValore();		
		popolaMazzo();
	}	

	/*
	 * Permette di associare un valore ad ogni tipo di carta
	 */
	private void associaValore(){
		/*
		 *  Con l'indice i itero sul tipo di carte, figure escluse;
		 *  con l'indice j assegno il valore
		 */
		for(int i=0, j=1; i<8; i++, j++)
			valore.put(TipoCarta.values()[i], new Double(j));
	
		valore.put(TipoCarta.Donna, 0.5);
		valore.put(TipoCarta.Fante, 0.5);
		valore.put(TipoCarta.Re, 0.5);
	}
	
	/*
	 * Creazione del mazzo di carte napoletane, 
	 * attraverso l'avvaloramento di ciascuna carta da gioco componente
	 */
	private void popolaMazzo(){
		// Per ogni tipo di carta
		for(int i = 0; i < TipoCarta.values().length; i++)
		{
			// Ricavo il tipo di carta in esame
			TipoCarta tmp = TipoCarta.values()[i];
			
			// Inserisco 4 volte lo stesso tipo di carta, uno per ogni seme
			mazzoCarte[i] = new Carta(Seme.Denari, tmp, valore.get(tmp));
			mazzoCarte[i+10] = new Carta(Seme.Coppe, tmp, valore.get(tmp));
			mazzoCarte[i+20] = new Carta(Seme.Bastoni, tmp, valore.get(tmp));
			mazzoCarte[i+30] = new Carta(Seme.Spade, tmp, valore.get(tmp));
		}
	}
		
}