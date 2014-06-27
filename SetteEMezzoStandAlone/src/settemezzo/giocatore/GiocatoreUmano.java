package settemezzo.giocatore;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp GiocatoreUmano</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un giocatore 
 * per il gioco del Sette e mezzo con logica gestita dall'utente. 
 * Puo' essere utilizzato sia per impersonare il banco che lo sfidante</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class GiocatoreUmano extends Giocatore{
		
	private static final long serialVersionUID = 5019300409089841926L;
	private boolean altraCarta = false;
	private boolean altraMano = false;
	
	/**
	 * Crea un Giocatore con logica gestita interamente dall'utente
	 */
	public GiocatoreUmano(){
		ruolo = SFIDANTE;
	}
	
	/**
	 * Imposta la scelta del Giocatore di voler un'altra carta
	 * @param altraCarta La scelta del Giocatore
	 */
	public void setAltraCarta(boolean altraCarta){
		this.altraCarta = altraCarta;
	}

	/**
	 * Imposta la scelta del Giocatore di voler proseguire il gioco
	 * @param altraMano La scelta del Giocatore
	 */
	public void setAltraMano(boolean altraMano){
		this.altraMano = altraMano;
	}
	
	@Override
	public double daiPuntiScoperti(){
		return punteggio - punteggioCoperto;		
	}
	
	@Override
	public boolean altraCarta(double puntoAvversario){
		return altraCarta;
	}
	
	@Override
	public boolean altraMano(){
		return altraMano;
	}
	
	@Override
	public void setPuntata(int puntata){
		this.puntata = puntata;
	}
	
	@Override
	public void reset(){
		super.reset();
		altraCarta = false;
		altraMano = false;
	}

	@Override
	public String daiTipoGioc() {
		return "umano";
	}
	
}