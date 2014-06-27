package settemezzo.util.database;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbspAlias</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Contiene tutti 
 * i nomi delle colonne delle tabelle presenti nel database setteemezzo</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Alias {
	
	/**
	 * Nome della tabella <code>setteemezzo.transazione</code>
	 */
	public final static String TABLE_TRANS = new String("transazione");
	
		/**
		 * Nome usato per riferirsi al campo partita 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String IDPARTITA = new String("id_partita");
		
		/**
		 * Nome usato per riferirsi al campo giocatore 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String GIOCATORE = new String("giocatore");
		
		/**
		 * Nome usato per riferirsi al campo ruolo 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String RUOLO = new String("ruolo");
		
		/**
		 * Nome usato per riferirsi al campo punteggio 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String PUNTEGGIO = new String("punteggio");
		
		/**
		 * Nome usato per riferirsi al campo vincita 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String VINCITA = new String("vincita");
		
		/**
		 * Nome usato per riferirsi al campo data 
		 * della tabella <code>setteemezzo.transazione</code> 
		 */
		public final static String DATA = new String("data");
		
	/**
	 * Nome della tabella <code>setteemezzo.utente</code>
	 */
	public final static String TABLE_USER = new String("utente");
	
		/**
		 * Nome usato per riferirsi al campo email 
		 * della tabella <code>setteemezzo.utente</code> 
		 */
		public final static String EMAIL = new String("email");
		
		/**
		 * Nome usato per riferirsi al campo nickname 
		 * della tabella <code>setteemezzo.utente</code> 
		 */
		public final static String NICKNAME = new String("nickname");
		
		/**
		 * Nome usato per riferirsi al campo password 
		 * della tabella <code>setteemezzo.utente</code> 
		 */
		public final static String PASSWORD = new String("password");
}