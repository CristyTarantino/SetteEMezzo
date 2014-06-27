package settemezzo.interfacciautente.componenti.calendario;

import java.awt.Component;
import java.awt.event.*;

import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Mese </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Crea un pannello 
 * nel quale inserisce un componente utile per la selezione dei mesi dell'anno</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

final class Mese extends JPanel implements ItemListener,ChangeListener {
	
	private static final long serialVersionUID = -2028361332231218527L;

	private Locale locale;
	private int mese;
	private int vecchioValoreSpinner = 0;
	
	// Necessari per il confronto
	private Giorno campoGiorno;
	private Anno campoAnno;
	
	private JComboBox comboBox;
	private JSpinner spinner;
	private boolean initialized;
	private boolean localInitialize;


	/**
	 * Construisce un oggetto spinner inserendo
	 * al suo interno un combo box
	 */
	public Mese() {
		
		// Creazione compoBox e relativa assegnazione dell'item Listener
		comboBox = new JComboBox();
		comboBox.addItemListener(this);
		
		/*
		 * Calcolo delle impostazioni di localizzazione 
		 * della macchina su cui gira il programma
		 */
		locale = Locale.getDefault();
		
		// Inizializzazione della lista della comboBox
		initNomeMesi();

		// Creazione di uno spinner e relativa assegnazione dell'item Listener
		spinner = new JSpinner();		
		spinner.addChangeListener(this);
		
		// Associazione del comboBox come editor per lo spinner
		spinner.setEditor(comboBox);
		comboBox.setRenderer(new DefaultListCellRenderer() {
			
			private static final long serialVersionUID = 4718234280952056062L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				
				// Imposta a destra la sistemazione del testo nella Combo
		        JLabel lbl = (JLabel)super.getListCellRendererComponent(
		                list, value, index, isSelected, cellHasFocus);
		        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		        return lbl;
			}
		});
		
		comboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Aggiunta di un bordo vuoto allo spinner
		spinner.setBorder(new EmptyBorder(0, 0, 0, 10));

		// Sistemazione dello spinner nella parte destra del pannello
		add(spinner);
	
		// Imposta il componente come inizializzato
		initialized = true;
		
		/*
		 * Seleziona il mese corrente come valore da visualizzare
		 * nel componente
		 */
		setMese(Calendar.getInstance().get(Calendar.MONTH));
	}

	/**
	 * Inizializza una lista di nomi, 
	 * specifici per la localizzazione della macchina su cui gira il programma,
	 * relativi ai mesi dell'anno
	 */
	public void initNomeMesi() {
		// Indica che non e' stata inizializzato il valore di localizzazione
		localInitialize = true;
		
		/*
		 * DateFormatSymbols e' una classe pubblica per incapsulare 
		 * dati relativi alla formattazione della data e dell'ora, 
		 * come ad esempio dati relativi ai nomi dei mesi, dei giorni della settimana, 
		 * e del fuso orario. 
		 */
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		
		/*
		 * Dall'oggetto sopra citato, vengono estratti i nomi dei dodici mesi
		 * e inseriti in un array di stringhe
		 */
		String[] nomiMesi = dateFormatSymbols.getMonths();
		
		/*
		 * Reset della comboBox
		 * Se il numero degli elementi della combo box e'
		 * uguale a 12, vengono rimossi tutti gli elementi dalla lista
		 */
		if (comboBox.getItemCount() == 12) {
			comboBox.removeAllItems();
		}
		
		/*
		 * La lista degli elementi della combo box viene popolata dalle
		 * stringhe relative ai mesi e contenute dall'arraylist nomiMesi
		 */
		for (int i = 0; i < 12; i++) {
			comboBox.addItem(nomiMesi[i]);
		}
		
		// Indica che e' stata inizializzato il valore di localizzazione
		localInitialize = false;
		
		// Nella comboBox verra' visualizzato il mese con indice mese
		comboBox.setSelectedIndex(mese);
	}

	// Invocato se il valore dell' spinner cambia
	@Override
	public void stateChanged(ChangeEvent e) {
		/*
		 * Quando lo stato dello spinner viene modificato,
		 * si vicava il valore relativo al mese che e' stato selezionato
		 */
		SpinnerNumberModel model = (SpinnerNumberModel) ((JSpinner) e
				.getSource()).getModel();
		
		// Tale valore viene trasformato in un numero e castato ad intero
		int value = model.getNumber().intValue();
		
		/*
		 * Se il valore e' maggiore del vecchio valore selezionato
		 * il predicato incrementa avra' valore true, false altrimenti
		 */
		boolean incrementa = (value > vecchioValoreSpinner) ? true : false;
		
		/*
         * Si porta il vecchio valore di mese al valore attuale
         * per poter modificare eventualmente la proprieta'
         * "mese"
         */
		vecchioValoreSpinner = value;
	
		// Si ricava il valore del mese selezioanto
		int mese = daiMese();

		 // Se il valore e' maggiore del vecchio valore selezionato
		if (incrementa) 
		{
			
			// Si incrementa il valore di mese
			mese += 1;

			// Se il valore di mese e' uguale a 12
			if (mese == 12) 
			{
				
				// Si resetta il valore di mese
				mese = 0;
				
				// Se il campo anno non e' nullo
				if (campoAnno != null) 
				{
					/*
					 * Incrementa il valore di anno, poiche' al 13° mese si e'
					 * nell'anno successivo. 
					 * Si ricava l'anno, si incrementa tale valore
					 * e lo si setta al campo anno
					 */
					int anno = campoAnno.daiAnno();
					anno += 1;
					campoAnno.setAnno(anno);
				}
			}
		} 
		else 
		{
			//  Se il valore e' minore del vecchio valore selezionato decrementa mese
			mese -= 1;
			
			// Se mese e' minore di 0 
			if (mese == -1) 
			{
				// Si pone mese a 11 (array va da 0 a 11)
				mese = 11;
				
				// Se il campo anno non e' nullo
				if (campoAnno != null) 
				{
					/* Decrementa il valore di anno, poiche' si e' selezionato
					 * il 12° mese nell'anno precedente. 
					 * Si ricava l'anno, si decrementa tale valore
					 * e lo si setta al campo anno
					 */
					int anno = campoAnno.daiAnno();
					anno -= 1;
					campoAnno.setAnno(anno);
				}
			}
		}

		// Setta il valore del mese
		setMese(mese);
	}

	// Invocato se lo stato della combo box cambia
	@Override
	public void itemStateChanged(ItemEvent e) {
		/*
		 * Se l'elemento che e' stato modificato e' proprio quello selezionato 
		 * prende l'indice relativo al mese selezionato...
		 */
		if (e.getStateChange() == ItemEvent.SELECTED) {
			int index = comboBox.getSelectedIndex();
			
			/*
			 * Se questo e' maggiore o uguale a zero 
			 * ed e' diverso dall'indice del mese correntemente visualizzato
			 * modifica solo il campo giorno
			 */
			if ((index >= 0) && (index != mese)) {
				setMese(index, false);
			}
		}
	}

	/**
	 * Associa il giorno scelto, al mese 
	 * @param campogiorno Il giorno da associare al mese
	 */
	public void setCampoGiorno(Giorno campogiorno) {
		this.campoGiorno = campogiorno;
	}

	/**
	 * Associa l'anno scelto, al mese 
	 * @param campoAnno L'anno da associare al mese
	 */
	public void setCampoAnno(Anno campoAnno) {
		this.campoAnno = campoAnno;
	}

	/**
	 * Restituisce l'indice del mese selezionato
	 * @return Il valore dell'indice del mese selezionato
	 */
	public int daiMese() {
		return mese;
	}

	/**
	 * Seleziona il mese di default da visualizzare nella comboBox.
	 * @param newMese Il mese di default da visualizzare nella comboBox
	 */
	public void setMese(int newMese) {
		/*
		 * Se il numero del mese passato e' minore di 0 o uguale al valore minimo
		 * previsto per un intero, viene selezionato il mese con indice zero e viene
		 * indicato come da mostrare selezionato nella combo Box.
		 * Se il numero del mese passato e' maggiore di 11, 
		 * viene svolta la stessa procedura passando pero' il mese con indice 11.
		 * Altrimenti l'operazione viene svolta per il numero passato come argomento,
		 * senza alcuna forzatura
		 */
		if (newMese < 0 || newMese == Integer.MIN_VALUE) 
			setMese(0, true);
		else if (newMese > 11) 
			setMese(11, true);
		else 
			setMese(newMese, true);
	}

	/**
	 * Seleziona il mese nella comboBox
	 * @param newMese L'indice del mese che e' stato selezionato
	 * @param selezionato true, se il mese deve essere evidenziato nella comboBox
	 */
	private void setMese(int newMese, boolean selezionato) {
		/*
		 * Se la combo box non e' stata ancora inizializzata,
		 * ne tantomeno localizzata la macchina
		 */
		if (!initialized || localInitialize) {
			return;
		}
	
		int oldMonth = mese;
		mese = newMese;
		
		/*
		 * Se e' stato selezionato il mese con indice newMese,
		 * mostra nella comboBox il mese con l'indice selezionato
		 */
		if (selezionato) {
			comboBox.setSelectedIndex(mese);
		}
		
		/* 
		 * Se il campo relativo al Giorno, non e' nullo,
		 * associa a quel giorno il mese indicato
		 */
		if (campoGiorno != null) {
			campoGiorno.setMese(mese);
		}
		
		/* 
		 * Modifica il vecchio valore della proprieta' mounth del pannello,
		 * con la nuova
		 */
		firePropertyChange("mese", oldMonth, mese);
	}
}