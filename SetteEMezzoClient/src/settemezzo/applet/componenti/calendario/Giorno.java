package settemezzo.applet.componenti.calendario;
import java.awt.*;
import java.awt.event.*;

import java.text.DateFormatSymbols;

import java.util.*;

import javax.swing.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Giorno </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che
 * crea un pannello il quale contiene i bottoni relativi ai giorni 
 * di uno specifico mese dell'anno </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
final class Giorno extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5181282795623016502L;

	private JButton[] giorni;

	private JButton giornoSelezionato;

	private JPanel pannelloGiorno;

	private int giorno;

	private Color coloreDefaultGiorno;

	private Color coloreElementoSelezionato;

	private Color sfondoDomenica;

	private Color decorationColoreSfondo;

	private String[] nomiGiorni;

	private Calendar calendar;

	private Calendar oggi;

	private Locale locale;

	private Date dataMinSelezionabile;

	private Date dataMaxSelezionabile;

	/**
	 * Costruttore del pannello contenente i bottoni relativi ai giorni del mese selezionato
	 * @param numClic Il numero di click necessari per sollevare un evento
	 */
	Giorno(final int numClic) {
		setLayout(new BorderLayout());
		setBackground(Color.blue);
		
		locale = Locale.getDefault();
		giorni = new JButton[49];
		giornoSelezionato = null;
		calendar = Calendar.getInstance(locale);
		oggi = (Calendar) calendar.clone();	

		pannelloGiorno = new JPanel();
		pannelloGiorno.setLayout(new GridLayout(7, 7));

		sfondoDomenica = new Color(164, 0, 0);
		
		// colore sfondo nomi dei giorni della settimana
		decorationColoreSfondo = new Color(210, 228, 238);

		// Riempie il pannello con una matrice di bottoni
		for (int y = 0; y < 7; y++) // Righe
		{
			for (int x = 0; x < 7; x++) //Colonne
			{
				int index = x + (7 * y);
				
				// La prima riga ha sfondo celeste
				if (y == 0) // Bottoni non rispondenti ad alcuna azione
					giorni[index] = new DecoratorButton();
				else 
				{
					giorni[index] = new JButton("x") {
						private static final long serialVersionUID = -7433645992591669725L;

						public void paint(Graphics g) 
						{
							// Su windows i componenti reagiscono diversamente ai metodi
							if ("Windows".equals(UIManager.getLookAndFeel().getID())) 
							{
								if (giornoSelezionato == this) {
									g.setColor(coloreElementoSelezionato);
									g.fillRect(0, 0, getWidth(), getHeight());
								}
							}
							super.paint(g);
						}
					};
					
					giorni[index].addActionListener(this);
					
					/*
					 * Se il numero di click è uguale a quelli per cui l'utilizzatore
					 * del calendario richiede un'azione, 
					 * imposta il giorno scelto dall'utente e
					 * indica al metodo chiamante che è stata modificata la 
					 * proprieta' "scelta"
					 */
					if(numClic !=0)
					{
						giorni[index].addMouseListener(new MouseAdapter()
						{
						     public void mouseClicked(MouseEvent e)
						     {
						      if (e.getClickCount() == numClic)
						      {
						    	  setGiorno(giorno);
						    	  firePropertyChange("scelta", false, true);
						      }
						     }
						 } );
					}
				}

				giorni[index].setMargin(new Insets(0, 0, 0, 0));
				giorni[index].setFocusPainted(false);
				
				// In fine aggiunge il bottone al pannello contenente i giorni
				pannelloGiorno.add(giorni[index]);
			}
		}

		// Imposta la data minima e massima da visualizzare nel calendario
		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.set(1, 0, 1, 1, 1);
		dataMinSelezionabile = tmpCalendar.getTime();
		tmpCalendar.set(9999, 0, 1, 1, 1);
		dataMaxSelezionabile = tmpCalendar.getTime();

		init();

		// Imposta il giorno corrente
		setGiorno(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		add(pannelloGiorno, BorderLayout.CENTER);
	}

	/**
	 * Imposta il testo dei pulsanti:
	 * i primi 7 pulsanti con i nomi dei giorni della settimana
	 * abbreviavi, secondo la localizzazione di riferimento;
	 * i rimanenti con i numeri del mese
	 */
	void init() {
		JButton testButton = new JButton();
		coloreDefaultGiorno = testButton.getBackground();
		coloreElementoSelezionato = new Color(160, 160, 160);

		Date date = calendar.getTime();
		calendar = Calendar.getInstance(locale);
		calendar.setTime(date);

		drawDayNames();
		drawDays();
	}

	/**
	 * Imposta i nomi dei giorni della settimana
	 */
	void drawDayNames() {
		
		// Imposta il formato della data
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		
		/*
		 * Individua il nomi dei giorni della settimana 
		 * abbreviati secondo la locazione di riferimento
		 */
		nomiGiorni = dateFormatSymbols.getShortWeekdays();
		
		/*
		 * Il primo bottone del pannello ha valore pari al primo giorno della settimana.
		 * Il primo giorno della settimana in Europa e' il lunedì, 
		 * che in java corrisponde al numero 2
		 */
		int giorno = calendar.getFirstDayOfWeek();

		// Per tutti i primi 7 bottoni del pannello 
		for (int i = 0; i < 7; i++) 
		{
			
			/* 
			 * Imposta il testo del bottone con quello
			 * presente nel relativo array dei nomi dei giorni della settimana
			 */
			giorni[i].setText(nomiGiorni[giorno]);
			
			// Se il numero del giorno della settimana e' uguale ad 1 lo evidenzia
			if (giorno == 1)
				giorni[i].setForeground(sfondoDomenica);

			/*
			 * Se il numero del giorno e' minore di 7 incrementa lo incrementa,
			 * altrimenti
			 */
			if (giorno < 7)
				giorno++;
			else
				giorno -= 6;
		}
	}
	
	/*
	 * Trace:
	 *  i=0, giorno=2, giorni[0].setText(nomeGiorni[2]=lun)
	 *  i=1, giorno=3, giorni[1].setText(nomeGiorni[3]=mar)
	 *  i=2, giorno=4, giorni[2].setText(nomeGiorni[4]=mer)
	 *  i=3, giorno=5, giorni[3].setText(nomeGiorni[5]=gio)
	 *  i=4, giorno=6, giorni[4].setText(nomeGiorni[6]=ven)
	 *  i=5, giorno=7, giorni[5].setText(nomeGiorni[7]=sab)
	 *  
	 *  giorno 7, entra nell'else 7-6 = 1
	 *  
	 *  i=6, giorno=1, giorni[6].setText(nomeGiorni[1]=dom)
	 *  
	 *  dom in rosso
	 */

	/**
	 * Nasconde o mostra i bottoni relativi ai giorni
	 */
	void drawDays() {
		
		// Clona il calendario
		Calendar tmpCalendar = (Calendar) calendar.clone();
		
		// Sul calendario clonato imposta il valore delle ore, minuti ...
		tmpCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalendar.set(Calendar.MINUTE, 0);
		tmpCalendar.set(Calendar.SECOND, 0);
		tmpCalendar.set(Calendar.MILLISECOND, 0);

		// Imposta la data minima selezionabile e i valori ad essa connessi
		Calendar minCal = Calendar.getInstance();
		minCal.setTime(dataMinSelezionabile);
		minCal.set(Calendar.HOUR_OF_DAY, 0);
		minCal.set(Calendar.MINUTE, 0);
		minCal.set(Calendar.SECOND, 0);
		minCal.set(Calendar.MILLISECOND, 0);

		// Imposta la data minima selezionabile e i valori ad essa connessi
		Calendar maxCal = Calendar.getInstance();
		maxCal.setTime(dataMaxSelezionabile);
		maxCal.set(Calendar.HOUR_OF_DAY, 0);
		maxCal.set(Calendar.MINUTE, 0);
		maxCal.set(Calendar.SECOND, 0);
		maxCal.set(Calendar.MILLISECOND, 0);

		/*
		 * Ricava il primo giorno della settimana per la locazione di riferimento
		 * N.B. In Europa e' 2
		 */
		int primoGiornoSettimana = tmpCalendar.getFirstDayOfWeek();
		
		// Imposta il primo giorno del mese
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);

		/*
		 * Imposta il valore del primo bottone relativo ai giorni del mese,
		 * che è dato dal numero del giorno della settimana del primo giorno del mese
		 * meno in numero del primo giorno della settimana (Europa 2)
		 */
		int bottoniNascosti = tmpCalendar.get(Calendar.DAY_OF_WEEK) - primoGiornoSettimana;

		/*
		 * Se il numero del primo bottone relativo al giorno del mese e'
		 * minore di zero (per esempio il mese comincia di domenica: 
		 * 1 (num. relativo alla Domenica) - 2 
		 * (num. relarivo al primo giorno della settimana) = -1
		 * A -1 e' necessario aggiungere il numero dei giorni della settimana (+7)
		 * per far nascondere i primi sei bottoni 
		 */
		if (bottoniNascosti < 0)
			bottoniNascosti += 7;

		int i;

		/*
		 * Per tutti i numeri dei bottoni che vanno nascosti
		 * imposta i bottoni come non visibili e imposta testo vuoto
		 */
		for (i = 0; i < bottoniNascosti; i++) 
		{
			/*
			 *  +7 perchè i primi 7 bottoni sono impegnati 
			 *  a contenere i nomi dei giorni della settimana
			 */
			giorni[i + 7].setVisible(false);
			giorni[i + 7].setText("");
		}
		
		// Si sposta temporalmente al mese successivo...
		tmpCalendar.add(Calendar.MONTH, 1);

		// ...ricavando il primo giorno del prossimo mese
		Date primoGiornoDelProssimoMese = tmpCalendar.getTime();
		
		// Riporta il calendario al mese corrente
		tmpCalendar.add(Calendar.MONTH, -1);
		
		// Ricava la data del giorno corrente
		Date giorno = tmpCalendar.getTime();
		
		int n = 0;
		
		Color coloreTestoBottone = getForeground();
		
		// Fintantoche' il giorno in esame precede il primo giorno del mese prossimo
		while (giorno.before(primoGiornoDelProssimoMese)) 
		{
			/*
			 * Inizia a scrivere il giorno e a rendere i bottoni visibili
			 * dal primo giorno non vuoto del mese + 7 (perche' 7 sono i bottoni relativi
			 * al nome dei giorni della settimana) + il numero del giorno effettivo.
			 * Riprendendo l'esempio precedentemente apportato:
			 * i=7, perchè il mese iniziava di domenica.
			 * Si iniziera' a scrivere dal 7(per i gioni della settimana)+ 7(=i) + 0(=n).
			 * Poiche la numerazione dei mesi inizia da uno, si scrivera' nel testo del
			 * bottone 0 + 1.
			 */
			giorni[i + n + 7].setText(Integer.toString(n + 1));
			giorni[i + n + 7].setVisible(true);

			/*
			 * Se il giorno che si sta disegnando corrisponde al giorno e all'anno
			 * odierno evidenzia di rosso il giorno, altrimenti di nero
			 */
			if ((tmpCalendar.get(Calendar.DAY_OF_YEAR) == oggi.get(Calendar.DAY_OF_YEAR))
					&& (tmpCalendar.get(Calendar.YEAR) == oggi.get(Calendar.YEAR))) 
				giorni[i + n + 7].setForeground(sfondoDomenica);
			else
				giorni[i + n + 7].setForeground(coloreTestoBottone);

			/*
			 * Se il numero del giorno che si sta disegnando corrisponde con quello
			 * cliccato dall'utente evidenzia il bottone modificando il suo sfondo
			 * e salva il bottone selezionato
			 */
			if ((n + 1) == this.giorno) 
			{
				giorni[i + n + 7].setBackground(coloreElementoSelezionato);
				giornoSelezionato = giorni[i + n + 7];
			} 
			else // altrimenti imposta il colore dello sfondo a quello di default
				giorni[i + n + 7].setBackground(coloreDefaultGiorno);

			/*
			 *  Se il giorno che stiamo disegnando viene prima 
			 *  della data minima o dopo la data massima considerata,
			 *  imposta il bottone come non selezionabile
			 */
			
			if (tmpCalendar.before(minCal) || tmpCalendar.after(maxCal)) 
				giorni[i + n + 7].setEnabled(false);
			else // altrimenti impostalo come cliccabile
				giorni[i + n + 7].setEnabled(true);

			// incrementa n
			n++;
			
			// Va alla data successiva a quella in esame nel calendario
			tmpCalendar.add(Calendar.DATE, 1);
			
			// Imposta il giorno da considerare al prossimo ciclo 
			giorno = tmpCalendar.getTime();
		}

		/*
		 * Quando raggiunge il primo giorno del prossimo mese, 
		 * cioe' ha terminato il ciclo, tutti bottoni che vanno dall'ultimo disegnato
		 * al 48-esimo li disegna come vuoti
		 */
		for (int k = n + i + 7; k < 49; k++) {
			giorni[k].setVisible(false);
			giorni[k].setText("");
		}
	}

	/**
	 * Imposta il giorno selezionato nel calendario
	 * @param nuovoGiorno Il giorno selezionato
	 */
	void setGiorno(int nuovoGiorno) {
		if (nuovoGiorno < 1) 
			nuovoGiorno = 1;

		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		tmpCalendar.add(Calendar.MONTH, 1);
		tmpCalendar.add(Calendar.DATE, -1);

		int maxGiornoMese = tmpCalendar.get(Calendar.DATE);

		if (nuovoGiorno > maxGiornoMese)
			nuovoGiorno = maxGiornoMese;

		int vecchioGiorno = this.giorno;
		this.giorno = nuovoGiorno;

		// Imposta il colore di default al giorno che precedentemente era selezionato
		if (giornoSelezionato != null) 
		{
			giornoSelezionato.setBackground(coloreDefaultGiorno);
			giornoSelezionato.repaint();
		}

		/*
		 * Scorre tutti i bottoni e quando trova quello che ha testo
		 * uguale a quello del nuovo giorno, ne evidenzia l'aspetto
		 */
		for (int i = 7; i < 49; i++) 
		{
			if (giorni[i].getText().equals(Integer.toString(this.giorno))) 
			{
				giornoSelezionato = giorni[i];
				giornoSelezionato.setBackground(coloreElementoSelezionato);
				break;
			}
		}
		
		// Notifica al chiamante che e' stato modificato la proprieta' "giorno"
		firePropertyChange("giorno", vecchioGiorno, this.giorno);
		
	}

	/**
	 * Imposta un nuovo mese. Necessario per una corretta rappresentazione grafica
	 * dei giorni 
	 * @param nuovoMese Il nuovo mese
	 */
	void setMese(int nuovoMese) {
		calendar.set(Calendar.MONTH, nuovoMese);
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int regGiorno = giorno;
		if (giorno > maxDays) 
		{
			regGiorno = maxDays;
			setGiorno(regGiorno);
		}

		drawDays();
	}

	/**
	 * Imposta un nuovo anno. Necessario per una corretta rappresentazione grafica
	 * dei giorni 
	 * @param nuovoAnno Il nuovo anno
	 */
	void setAnno(int nuovoAnno) {
		calendar.set(Calendar.YEAR, nuovoAnno);
		drawDays();
	}

	/**
	 * Imposta un nuovo mese. Necessario per una corretta rappresentazione grafica
	 * dei giorni 
	 * @param nuovoCalendario Il nuovo Calendario
	 */
	void setCalendar(Calendar nuovoCalendario) {
		this.calendar = nuovoCalendario;
		drawDays();
	}

	// Chiamato quando l'utente seleziona un giorno diverso da quello di default
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		int giornoSel = new Integer(buttonText).intValue();
		setGiorno(giornoSel);
	}

	private class DecoratorButton extends JButton {
		private static final long serialVersionUID = -5306477668406547496L;

		public DecoratorButton() {
			setBackground(decorationColoreSfondo);
			setContentAreaFilled(true);
			setBorderPainted(false);
		}
	}
}