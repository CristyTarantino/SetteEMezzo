package settemezzo.util.database;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp PartitaDAOFactory</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Factory che permette 
 * la creazione di istanze di PartitaDAO a seconda del Dbms utilizzato</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class PartitaDAOFactory {
	
	/**
	 * Nome che rappresenta il dbms MySql
	 */
	public final static String MYSQL = new String("mysql");
	
	/**
	 * Restituisce una classe di comunicazione con il database, 
	 * a seconda del dbms utilizzato nell'applicazione
	 * @return Classe di comunicazione con la base di dati
	 */
	public static iPartitaDAO getInstance(String dbms){
		
		/*
		 *  Nel nostro caso ritorna la classe associata al dbms MySql.
		 *  In caso di + db, dovremmo aggiungere un altro if 
		 *  e un'altra costante pubblica  
		 */
		if(dbms.equals(MYSQL))
			return new TransazioneDB();
		else
			return null;
	}
}