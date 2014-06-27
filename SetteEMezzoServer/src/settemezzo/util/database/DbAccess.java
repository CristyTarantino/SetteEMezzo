package settemezzo.util.database;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp DbAccess</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Permette di gestire 
 * la connessione ad una base di dati, ricavando i dati della connessione da un file xml</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class DbAccess {
	
	// Le informazioni necessarie alla connessione al database
	private static String DBMS;
	private static String DRIVER_CLASS_NAME;
	private static String USER_ID;
	private static String PASSWORD;
	private static String SERVER;
	private static String DATABASE;
	private static String PORT;
	
	// La connessione con database
	private static Connection conn;

	/**
	 * Provvede al caricamento del driver 
	 * ed all'inizializzazione della connessione
	 * con i dati presenti nel file xml passato come parametro
	 * @param file Il nome del file xml (estensione inclusa)
	 */
	public static void initConnection(String file){
		caricaDati(file);
		
		try
		{
			// Registrazione del driver manager
			Class.forName(DRIVER_CLASS_NAME);
			String url = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
			
			// Connessione al db
			conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
		}
		catch(Exception e)
		{
			System.err.println("Errore driver!!!");
		}
	}
	
	/**
	 * Restituisce la connessione alla base di dati
	 * @return La connessione alla base di dati
	 */
	public static Connection getConnection(){
		return conn;
	}
	
	/**
	 * Consente la chiusura della connessione alla base di dati
	 */
	public static void closeConnection(){
		try 
		{
			conn.close();
		}
		catch (SQLException e) 
		{
			System.err.println("Errore nella chiusura della connessione!");
		}
	}
	
	/*
	 * Consente il caricamento di tutte le informazioni 
	 * necessarie alla connessione alla base di dati 
	 */
	private static void caricaDati(String file){
		Properties datiInConn = new Properties();
		
		try
		{
			/*
			 * Dal Class Object di questa classe (DbAccess) si ricava l'oggetto loader
			 * responsabile del caricamento della medesima classe
			 */
			ClassLoader loader = new DbAccess().getClass().getClassLoader();
			
			// Ricavo l'input stream del file xml passato come parametro
			InputStream url = loader.getResourceAsStream(file);
			
			// Conversione del file xml in un oggetto Properties
			datiInConn.loadFromXML(url);
			url.close();
			
			/*
			 *  Interrogazione dell'oggetto Properties
			 *  per ricavare i dati della connessione 
			 */
			DBMS = datiInConn.getProperty("Dbms");
			DRIVER_CLASS_NAME = datiInConn.getProperty("Class_Name");
			USER_ID = datiInConn.getProperty("User_ID");
			PASSWORD = datiInConn.getProperty("Password");
			SERVER = datiInConn.getProperty("Server");
			DATABASE = datiInConn.getProperty("Database");
			PORT = datiInConn.getProperty("Port");
		}
		catch(Exception e)
		{
			// NullPointerException - InvalidPropertiesFormatException - IOException
			// tutte eccezioni sollevate da loadFromXML
			System.err.println(e.getMessage());
		}
	}
}