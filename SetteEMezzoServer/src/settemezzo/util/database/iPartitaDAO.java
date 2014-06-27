package settemezzo.util.database;

import java.sql.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iPartitaDAO</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Interfaccia che definisce
 * i metodi d'interazione al database che conserva i salvataggi della partita
 * (setteemezzo.transazione) e gli utenti registrati al sistema (setteemezzo.utente)</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public interface iPartitaDAO {
	
	/**
	 * Aggiunge una transazione al database
	 * @param tupla La transazione da aggiungere
	 */
	public void addTransazione(PartitaEntry tupla);
	
	/**
	 * Recupera lo storico delle partite in una determinata data per un determinato giocatore
	 * @param data La data per cui si richiede lo storico, nel formato yyyy-MM-dd
	 * @param mail L'email del giocatore per cui si richiede la query
	 * @return Una lista di record che soddisfano i criteri di ricerca
	 */
	public ArrayList<PartitaEntry> getTransazioni(String data, String mail);
	
	/**
	 * Aggiunge un utente al database
	 * @param tupla L'utente da aggiungere
	 */
	public void addUtente(UtenteEntry tupla);
	
	/**
	 * Recupera le informazioni relative all'utente
	 * @param email L'email dell'utente
	 * @param password La password dell'utente
	 * @return le informazioni sull'utente
	 */
	public UtenteEntry getUtente(String email, String password);
	
	/**
	 * Verifica l'esistenza della tupla, passata come argomento, 
	 * all'interno della tabella utente
	 * @param utenteVerify La tupla da verificare
	 * @return true se la tupla esiste, false altrimenti
	 */
	public boolean existUtente(UtenteEntry utenteVerify) throws SQLException ;
}