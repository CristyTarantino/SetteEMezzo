package settemezzo.applet.componenti.help;

import java.net.*;
import javax.swing.tree.*;

/**
 * <p><b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Nodo</p>
 * <p><b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p><b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione</p>
 * <p><b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un nodo
 * da usare in una struttura di dati ad albero, in particolare per la definizione
 * di un indice per un HelpFrame
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Nodo extends DefaultMutableTreeNode implements iNode {
	private static final long serialVersionUID = 3577747539937727286L;
	
	private String nomeNodo;
    private URL urlNodo;

    /**
     * Crea un nodo composto da un nome ed un URL.
     * @param nome il nome usato per la visualizzazione del nodo
     * @param nomeFile il nome del file html associato
     * nel formato <code>package_name/name_file.html</code>
     */
    public Nodo(String nome, String nomeFile) {
        // Ricavo il percorso del file html
        urlNodo = getClass().getClassLoader().getResource(nomeFile);
        nomeNodo = nome;
        
        // Se nessun file html viene trovato
        if (urlNodo == null)
            System.err.println("Impossibile trovare il file: "+ nomeFile);
    }

    @Override
	public URL getUrl() {
		return urlNodo;
	}
	
    @Override
    public String toString() {
        return nomeNodo;
    }
}