package settemezzo.util.database;

import java.sql.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp TransazioneDB</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Fornisce i metodi
 * di interazione al database relativo ai salvataggi della partita (setteemezzo_standalone.transazione)</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class TransazioneDB implements iPartitaDAO{
	// La connessione alla base di dati
	private Connection conn;
	
	/**
	 * Crea una connessione al database setteemezzo_standalone
	 */
	public TransazioneDB(){
		// Effettuo la connessione al database con i dati presenti nel file xml
		DbAccess.initConnection("mysql_standalone.xml");
		conn = DbAccess.getConnection();
	}
	
	@Override
	public void addTransazione(PartitaEntry tupla){
		// Creazione della query sql
		String query = "INSERT INTO " + Alias.TABLE_TRANS 
						+ "(" + Alias.GIOCATORE + "," + Alias.RUOLO + ","
							  + Alias.PUNTEGGIO + "," + Alias.VINCITA + "," 
							  + Alias.DATA + ")" 
						+ "VALUES (?, ?, ?, ?, ?)";  		
		try 
		{
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement =  conn.prepareStatement(query);
			
			// Impostazione dei parametri della query
			statement.setString(1, tupla.daiGiocatore());
			statement.setString(2, tupla.daiRuolo());
			statement.setDouble(3, tupla.daiPunteggio());
			statement.setString(4, tupla.daiVincita());
			statement.setObject(5, tupla.daiData());			
			
			// Esecuzione della query di modifica
			statement.executeUpdate();
			
			// Chiusura dello statement e della connessione
			statement.close();
			DbAccess.closeConnection();
			
		} 
		catch (SQLException e) 
		{
			System.err.println("Errore in aggiunta transazione");
		}
	} 
	
	@Override
	public ArrayList<PartitaEntry> getTransazioni(String data){
		// Creazione della query sql
		String query = "SELECT * FROM " + Alias.TABLE_TRANS 
						+ " WHERE " + Alias.DATA + " LIKE ?";
		
		ArrayList<PartitaEntry> listaTrans = new ArrayList<PartitaEntry>();
		
		try 
		{
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement = conn.prepareStatement(query);

			// Impostazione dei parametri della query
			statement.setString(1, data + "%");
			
			// Esecuzione della query di selezione
			ResultSet risultato = statement.executeQuery();
			
			// Fintantoche' ci sono tuple nel ResultSet ...
			while(risultato.next())
			{
				/*
				 *  ... recupera i dati dal ResultSet ed inseriscili
				 *  in un oggetto di PartitaEntry 
				 */
				PartitaEntry partTmp = new PartitaEntry();
				partTmp.setGiocatore(risultato.getString(Alias.GIOCATORE));
				partTmp.setRuolo(risultato.getString(Alias.RUOLO));
				partTmp.setPunteggio(risultato.getDouble(Alias.PUNTEGGIO));
				partTmp.setVincita(risultato.getString(Alias.VINCITA));
				partTmp.setData((java.util.Date) risultato.getObject(Alias.DATA));				
				
				listaTrans.add(partTmp);			
			}
			
			// Chiusura dello statement e della connessione
			statement.close();
			DbAccess.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.err.println("Errore in caricamento transazione");
		}
		
		return listaTrans;
	}
}