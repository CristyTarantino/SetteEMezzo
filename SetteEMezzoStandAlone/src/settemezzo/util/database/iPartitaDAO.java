package settemezzo.util.database;

import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iPartitaDAO</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Interfaccia che definisce
 * i metodi d'interazione al database che conserva i salvataggi della partita
 * (setteemezzo_standalone.transazione).</p>
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
	 * Recupera lo storico delle partite in una determinata data
	 * @param data La data per cui si richiede lo storico, nel formato yyyy-MM-dd
	 * @return Una lista di record che soddisfano i criteri di ricerca
	 */
	public ArrayList<PartitaEntry> getTransazioni(String data);
}