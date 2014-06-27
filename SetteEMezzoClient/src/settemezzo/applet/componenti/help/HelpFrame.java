package settemezzo.applet.componenti.help;

import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * <p><b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp HelpFrame</p>
 * <p><b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p><b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p><b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che permette 
 * la visualizzazione di un help</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class HelpFrame extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = -531310540905650595L;
	
	private JTree tree;
	private JEditorPane htmlPane;

    /**
     * Permette la creazione di una finestra di help
     * in cui visualizzare una guida utente
     * @param titolo Il titolo del finestra
     * @param icona L'icona del finestra.
     * @param indice Il nodo radice della struttura ad albero che
     * rappresenta l'indice dell'help
     */
    public HelpFrame(String titolo, Image icona, iNode indice) {
        super(titolo);
        setIconImage(icona);
        setLayout(new BorderLayout());

        // Creazione dell'indice grafico tramite il nodo ricevuto come parametro 
        tree = new JTree(indice);

        // Creazione di un JTree che permette una selezione per volta
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Aggiunta di un listener che gestisce il cambiamento della pagina da visualizzare
        tree.addTreeSelectionListener(this);

        // Creazione di uno scroll pane per l'indice
        JScrollPane vistaIndice = new JScrollPane(tree);

        // Creazione del pannello che permette di visualizzare gli html
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        
        // Creazione di uno scroll pane per il pannello dell'html
        JScrollPane vistaHtml = new JScrollPane(htmlPane);

        // Impostazione di uno split pane in modo da separare le due parti (indice, html)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Impostazione della posizione assoluta del divisore
        splitPane.setDividerLocation(200); 
        
        // Imposto dimensioni split pane
        splitPane.setMinimumSize(new Dimension(200, 500));
        splitPane.setPreferredSize(new Dimension(200, 500));
        
        // Assegnazione delle due aree
        splitPane.setLeftComponent(vistaIndice);
        splitPane.setRightComponent(vistaHtml);

        splitPane.setPreferredSize(new Dimension(800, 600));

        // Aggiunta dello split pane a questo JFrame
        add(splitPane);
        pack();
        
        // Imposto la modalita' di chiusura
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Visualizzo la prima pagina salvata nella radice del JTree
        displayURL(indice);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        /*
         * Ricavo l'ultimo nodo che e' stato selezionato.
         * Nel caso nessun nodo fosse stato selezionato,
         * il metodo ritornerebbe "null"
         */
    	DefaultMutableTreeNode nodo = (DefaultMutableTreeNode)
                           		tree.getLastSelectedPathComponent();
        
    	// Verifico se e' stato selezionato un nodo o meno
        if(nodo == null) 
        	return;

        /*
         * Se al nodo in esame e' stato associato un oggetto 
         * di tipo iNode mostro la pagina html relativa
         */
        if (nodo instanceof iNode)
        	displayURL(nodo);
    }

	/*
	 * Aggiorna la schermata relativa alle pagine html 
	 * con la pagina associata al nodo passato come parametro
	 * @param nodo Il nodo di cui bisogna visualizzare la pagina html
	 */
	private void displayURL(Object nodo) {
		// Ricavo l'url del nodo da visualizzare
		URL url = ( (iNode) nodo).getUrl();
		
		try 
	    {
	        if(url != null)
	            htmlPane.setPage(url);
	        else 
	        	htmlPane.setText("Pagina non trovata");
	    }
	    catch (IOException e) 
	    {
	        System.err.println("Formato url errato: " + url);
	    }
	}
}