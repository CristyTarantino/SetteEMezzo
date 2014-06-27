package settemezzo.applet.componenti.help;

import java.net.*;
import javax.swing.tree.*;

/**
 * <p><b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iNode</p>
 * <p><b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p><b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione</p>
 * <p><b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Interfaccia che definisce
 * i requisiti per un oggetto che pu&ograve; essere usato come nodo in un JTree.
 * Adatto alla creazione di un indice per una guida in linea</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public interface iNode extends TreeNode {
	
	/**
	 * Restituisce l'url della pagina html 
	 * che e' stata associata al nodo
	 * @return L'url del nodo
	 */
	public URL getUrl();
}