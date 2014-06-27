package settemezzo.giocatore;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp SfidanteCPU</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta uno sfidante 
 * per il gioco del Sette e mezzo con logica gestita dal computer </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class SfidanteCPU extends Giocatore{
	
	private static final long serialVersionUID = 5891202118187572584L;
	
	// Rappresenta il minimo punteggio che la CPU dovrebbe raggiungere per fermarsi
	private final static double CPUPUNTOMINIMO = 4.5;
	
	// Numero di mani che desidera giocare 
	private transient int nroMani = 0;
	
	/**
	 * Crea un nuovo Giocatore di tipo Banco per il gioco del Sette e Mezzo
	 */
	public SfidanteCPU(){
		nome = "CPU";
		ruolo = SFIDANTE;
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
		
		if(punteggio < CPUPUNTOMINIMO || punteggio < puntoAvversario)
			decisione = true;
		
		return decisione;	
	}
	
	@Override
	public boolean altraMano(){
		boolean decisione = false;
		
		//Incremento nroMani e poi lo valuto
		if(++nroMani < 5)
			decisione = true;
		
		return decisione;
	}
	
	@Override
	public void setPuntata(int puntata){
		this.puntata = credito/10 +1;
	}

	@Override
	public String daiTipoGioc() {
		return "cpu";
	}
}