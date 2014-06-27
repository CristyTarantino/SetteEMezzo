package settemezzo.interfacciautente.componenti;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp TabellaStorico</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Permette di visionare lo storico
 * delle partite giocate sotto forma tabellare </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class TabellaStorico extends JDialog {
	private static final long serialVersionUID = -9166887414436636219L;
	
	// La struttura grafica tabella
	private JTable tabella;
	
	// Array di Stringhe rappresentanti i nomi delle colonne
	private String[] nomiColonne;
	
	// La Matrice dei dati da inserire nella tabella
	private Object[][] dati;
	
	/**
	 * Costruisce una tabella per la visualizzazione di uno storico
	 * @param frame La finestra chiamante
	 * @param title Il titolodella finestra
	 * @param colonne Array contenente il nome delle colonne
	 * @param dati Matrice contenente i dati da visualizzare
	 */
	public TabellaStorico(JFrame frame, String title, String[] colonne, Object[][] dati) {
		super(frame, title, true);
		nomiColonne = colonne;
		this.dati = dati;
		
		// Costruzione della tabella attraverso un modello di tabella personalizzato
		tabella = new JTable(new TableModel());
		
		// Imposto la modalita' di ridimensionamento delle colonne
		tabella.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		/* 
		 * Imposto la dimensione iniziale della tabella 
		 * senza la quale non si necessita di una scrollbar
		 */
		tabella.setPreferredScrollableViewportSize(new Dimension(800, 200));
		
		// Imposto le colonne come spostabili tra loro 
		tabella.setDragEnabled(true);
		
		// Imposto le celle della tabella non editabili
		tabella.setEnabled(false);
		
		// Rendo tutte le colonne ordinabili
		tabella.setAutoCreateRowSorter(true);
		
		// Imposto i formattatori per le colonne che conterrano date, stringhe, double 
		tabella.setDefaultRenderer(java.util.Date.class, new DateRenderer());
		tabella.setDefaultRenderer(String.class, new StringRenderer());
		tabella.setDefaultRenderer(Double.class, new DoubleRenderer());
		
		JScrollPane scroll = new JScrollPane(tabella);
		getContentPane().add(scroll);
		
		setPreferredSize(new Dimension(800,500));
		setMinimumSize(new Dimension(800,500));
		pack();
		setLocationRelativeTo(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	/*
	 * Fornisce un table model object per gestire i dati presenti nella tabella
	 */
	private class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = -2945584307942587758L;
		
		/*
		 * Restituisce il numero delle colonne della tabella
		 * @return Il numero delle colonne
		 */
		public int getColumnCount() {
			return nomiColonne.length;
		}

		/*
		 * Restituisce il numero delle righe della tabella
		 * @return Il numero delle righe
		 */
		public int getRowCount() {
			return dati.length;
		}
		
		/*
		 * Restituisce il nome della colonna i-esima
		 * @param L'indice della colonna di cui si vuol conoscere il nome
		 * @return Il nome associato alla colonna
		 */
		@Override
		public String getColumnName(int col) {
			return nomiColonne[col];
		}
		
		/*
		 * Restituisce l'oggetto presente in una determinata cella
		 * @param row L'indice della riga della cella
		 * 		  col L'indice della colonna della cella
		 * @return L'oggetto presenta nella cella indicata
		 */
		public Object getValueAt(int row, int col) {
			return dati[row][col];
		}

		/*
		 * JTable usa questo metodo per determinare il formattatore/editore
		 * per ogni cella. Se tale metodo non viene implementato,
		 * una colonna che presenta valori booleani, avra' in ogni cella
		 * il testo "true"/"false" invece di una checkbox.
		 *  
		 * Restituisce la Classe degli elementi presenti
		 * in una determinata colonna
		 * @param index L'indice della colonna
		 * @return La Classe di appartenenza dei dati
		 */
		@Override
		public Class<?> getColumnClass(int index) {
			return getValueAt(0, index).getClass();
		}
	}
	
	/*
	 * Classe che permette di formatta i campi data 
	 * nel formato "dd-MM-yyyy - HH:mm:ss".
	 */
	private class DateRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -5279582673832017459L;

		private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
		
		/*
		 * Imposta il testo della JLabel usata per creare la cella
		 * @param value L'oggetto da formattare (nel nostro caso un oggetto di tipo Date)
		 */
		@Override
		public void setValue(Object value) {
			setHorizontalAlignment(CENTER);
			setText((value == null) ? "" : formatter.format(value));
	    }
	}
	
	/*
	 * Classe che permette di formatta i campi di tipo Double 
	 */
	private class DoubleRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -5279582673832017459L;
		
		/*
		 * Imposta il testo della JLabel usata per creare la cella
		 * @param value L'oggetto da formattare (nel nostro caso un oggetto di tipo Double)
		 */
		@Override
		public void setValue(Object value) {
			setHorizontalAlignment(RIGHT);
			setText(value.toString());
	    }
	}
	
	/*
	 * Classe che permette di formatta i campi di tipo stringa 
	 * assegnandoli un allineamento orizzontale.
	 */
	private class StringRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 5687689183306348969L;

		/*
		 * Imposta il testo della JLabel usata per creare la cella
		 * @param value L'oggetto da formattare (nel nostro caso una stringa)
		 */
		@Override
		public void setValue(Object value) {
			setHorizontalAlignment(CENTER);
			setText(value.toString());
	    }
	}
}