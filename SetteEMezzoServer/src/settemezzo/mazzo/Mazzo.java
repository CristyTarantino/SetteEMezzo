package settemezzo.mazzo;

import java.io.Serializable;
import java.util.*;

import settemezzo.util.exception.MazzoTerminatoException;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Mazzo</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un Mazzo 
 * di Carte generiche, opportunamente manipolabile </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public abstract class Mazzo implements Serializable{
	
	private static final long serialVersionUID = 7625282311538713292L;
	
	private int numCarte;
	protected Carta[] mazzoCarte;
	
	// Indica la prima carta del mazzo disponibile per la distribuzione
	protected int prossimaCarta;
			
	/**
	 * Crea un Mazzo di Carte di dimensione passata come argomento
	 * @param dimensione Numero di carte da cui il mazzo deve essere composto
	 */
	public Mazzo(int dimensione){
		mazzoCarte = new Carta[dimensione];
		numCarte = dimensione;
	}
	
	/**
	 * Restituisce la prossima carta estratta dal mazzo
	 * @return La prossima carta estratta dal mazzo
	 * @throws MazzoTerminatoException Eccezione sollevata in caso di mazzo terminato
	 */
	public Carta carta() throws MazzoTerminatoException {	
		
		/*
		 * Se l'indice della prossima carta da estrarre e' maggiore
		 * del numero di carte allora solleva l'eccezione MazzoTerminatoException;
		 * altrimenti se la prossima carta e' stata estratta, 
		 * quindi risulta gia' utilizzata, ...
		 */
		if(prossimaCarta >= numCarte)
			throw new MazzoTerminatoException();
		else if(mazzoCarte[prossimaCarta].isEstratta())
		{
			// ... incrementa l'indice della prossima carta e riesegui il metodo
			prossimaCarta++;
			return carta();
		}
		else
		{
			/*
			 * altrimenti, la carta non risulta estratta. 
			 * Dunque segna la carta come estratta, 
			 * incrementa l'indice della prossima carta e
			 * ritorna la carta appena estratta
			 */
			Carta proxCarta = mazzoCarte[prossimaCarta];
			proxCarta.cartaEstratta();
			prossimaCarta++;
			return proxCarta;
		}
	}
	
	/**
	 * Mescola il mazzo di carte resettando lo stato delle carte
	 */
	public void mescola(){
		prossimaCarta = 0; // resetta l'indice della prossima carta
		
		int j = 0;
		Carta temp = null;
		Random numGen = new Random();
		
		// Per ciascuna carta del mazzo
		for(int i = 0; i < numCarte; i++)
		{
			// calcola un numero a caso compreso tra 0 e numCarte
			j = numGen.nextInt(numCarte);
			
			// Se il numero scelto e' diverso dall'indice della carta da mescolare
			if (i != j)
			{
				// esegue lo swap
				temp = mazzoCarte[i];
				mazzoCarte[i] = mazzoCarte[j];
				mazzoCarte[j] = temp;
				
				// rende estraibili dal mazzo le carte in esame
				mazzoCarte[i].cartaNonEstratta();
				mazzoCarte[j].cartaNonEstratta();
			}
		}// fine for
		
	}
	
	/*
	 * Overloading del metodo mescola()
	 * Usando come argomento un contenitore di carte, 
	 * si lascia liberta' all'utilizzatore della classe Mazzo
	 * sul mescolamento di carte organizzate secondo una propria concettualizzazione 
	 */
	/**
	 * Mescola il Mazzo di Carte da gioco 
	 * lasciando inalterato lo stato delle carte in uso
	 * @param carteSulTavolo Le carte utilizzate durante una mano
	 */
	public void mescola(Collection<Carta> carteSulTavolo){
		
		// resetta l'indice della prossima carta
		prossimaCarta = 0;		
		int j = 0;
		Carta temp = null;
		Random numGen = new Random();
		
		// Per ciascuna carta del mazzo
		for (int i = 0; i < numCarte; i++)
		{
			// calcola un numero a caso compreso tra 0 e 40
			j = numGen.nextInt(numCarte);
			
			// Se il numero scelto e' diverso dall'indice della carta da mescolare
			if (i != j)
			{
				// esegue lo swap
				temp = mazzoCarte[i];
				mazzoCarte[i] = mazzoCarte[j];
				mazzoCarte[j] = temp;
				
				/*
				 * Se le carte che stiamo scambiando non sono in uso nella mano
				 * allora le sbloccheremo rendendole estraibili
				 */
				if(!carteSulTavolo.contains(mazzoCarte[i]))
					mazzoCarte[i].cartaNonEstratta();
				
				if(!carteSulTavolo.contains(mazzoCarte[j]))
					mazzoCarte[j].cartaNonEstratta();
			}
		}
	}
}