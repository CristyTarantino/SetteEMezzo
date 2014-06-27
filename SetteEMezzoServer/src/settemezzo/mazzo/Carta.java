package settemezzo.mazzo;

import java.io.Serializable;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Carta</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta una singola 
 * carta da gioco dotata di un seme, di un tipo identificativo e di un valore</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Carta implements Serializable{
	
	private static final long serialVersionUID = 1113882171337467041L;

	private Seme seme;
	private TipoCarta tipoCarta;
	private double valore;
	
	// Variabile che identifica se la carta e' stata estratta da un mazzo 
	private boolean estratta;
	
	/**
	 * Crea una nuova Carta da gioco dotata di
	 * un seme, un tipo ed un valore
	 * @param aSeme Il seme della carta da gioco
	 * @param aTipoCarta Il tipo della carta da gioco
	 * @param aValore Il valore della carta da gioco
	 */
	public Carta(Seme aSeme, TipoCarta aTipoCarta, double aValore){
		seme = aSeme;
		tipoCarta = aTipoCarta;		
		valore = aValore;
		estratta = false;
	}
	
	/**
	 * Restituisce il Seme della Carta da gioco
	 * @return Il seme della Carta
	 */
	public Seme daiSeme(){ 
		return seme;
	}
	
	/**
	 * Restituisce il Tipo della Carta da gioco
	 * @return Il tipo della Carta
	 */
	public TipoCarta daiTipoCarta(){
		return tipoCarta;
	}
			
	/**
	 * Restituisce il Valore della Carta da gioco
	 * @return Il valore della Carta
	 */
	public double daiValore(){
		return valore;
	}
	
	/**
	 * Identifica la Carta come estratta 
	 */
	public void cartaEstratta(){
		estratta = true;
	}
	
	/**
	 * Identifica la Carta come non ancora estratta
	 */
	public void cartaNonEstratta(){
		estratta = false;
	}
	
	/**
	 * Restituisce lo stato della Carta
	 * @return true se la Carta e' stata estratta, false altrimenti
	 */
	public boolean isEstratta(){
		return estratta;
	}
	
	/**
	 * Converte in stringa le informazioni relative alla carta da gioco
	 * @return Stringa con le informazioni sulla carta
	 */
	public String toString(){
		String infoCarta = tipoCarta + " di " + seme + ". Valore: "+ valore;
		return infoCarta;
	}
	
	/*
	 * Si e' ritenuto opportuno fare overriding del metodo equals di Object, poichè
	 * viene utilizzato nel metodo indexOf della classe ArrayList (Giocatore haMatta())
	 * e nel metodo contains dell'interfaccia Collection (Giocatore, Mazzo)
	 */
	/**
	 * Indica quando una Carta e' uguale ad un altra
	 * @param carta La carta con la quale effettuare il confronto
	 * @return true se la carta e' uguale alla carta passata come argomento, 
	 * 			false altrimenti
	 */
	@Override
	public boolean equals(Object carta){
		Carta tmp = (Carta) carta;
		
		/*
		 * Se la carta passata e' uguale per seme, tipo e valore alla carta
		 * che sto analizzando, allore le carte sono uguali
		 */
		if(tmp.seme == this.seme && 
				tmp.tipoCarta == this.tipoCarta && tmp.valore == this.valore)
			return true;
		else
			return false;			
	}
	
}