package settemezzo.giocatore;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp BancoCPU</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un banco 
 * per il gioco del Sette e mezzo con logica gestita dal computer </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class BancoCPU extends Giocatore{
	
	private static final long serialVersionUID = -342045341384821202L;
	
	// Rappresenta il minimo punteggio che la CPU dovrebbe raggiungere per fermarsi
	private final static double CPUPUNTOMINIMO = 4.5;
	
	/**
	 * Crea un Giocatore di tipo Banco per il gioco del Sette e Mezzo
	 */
	public BancoCPU(){
		nome = "CPU";
		ruolo = BANCO;
	}
	
	@Override
	public double daiPuntiScoperti(){
		double punteggioScoperto = 0.0;
		
		/*
		 * Per tutte le carte scoperte, il punteggio e' dato dalla somma 
		 * dei valori delle carte 
		 */
		if(carteSulTavolo.size()>1)
		{
			for (int i = 1; i < carteSulTavolo.size(); i++) 
				punteggioScoperto += carteSulTavolo.get(i).daiValore();
		}

		return punteggioScoperto;		
	}
	
	@Override
	public boolean altraCarta(double puntoAvversario){		
		boolean decisione = false;
		
		if(punteggio < CPUPUNTOMINIMO || punteggio < (puntoAvversario + MIN_PUNTEGGIO))
			decisione = true;
		
		return decisione;	
	}
	
	@Override
	public boolean altraMano(){
		return false;
	}
	
	@Override
	public void setPuntata(int puntata){
		// Il banco non punta mai
	}

	@Override
	public String daiTipoGioc() {
		return "cpu";
	}
}