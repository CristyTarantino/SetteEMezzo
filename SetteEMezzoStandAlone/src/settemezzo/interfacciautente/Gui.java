package settemezzo.interfacciautente;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import settemezzo.interfacciautente.componenti.*;
import settemezzo.interfacciautente.componenti.calendario.*;
import settemezzo.interfacciautente.util.*;
import settemezzo.mazzo.*;
import settemezzo.util.database.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Gui</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp L'interfaccia utente
 * grafica, che consente all'utente di interagire con l'applicazione
 * mediante finestre, icone, menu e dispositivo di puntamento (Interazione WIMP) </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class Gui extends JFrame implements iConsole{
	private static final long serialVersionUID = 1337282728608396825L;
	
	private final static String IMG_PATH = "images/";
	private final static String TITOLO = "Sette e Mezzo";
	private final static String GIOCATORE = "umano";
	
	// L'immagine rappresentante il dorso della carta coperta distribuita alla CPU
	private static JLabel DORSO_CARTA = new JLabel();
	private static Image ICON;
	
	// La carta coperta distribuita alla CPU
	private JLabel primaCartaCpu;
	
	/*
	 * La distanza che le carte nei vari Layer devono avere 
	 * dall'elemento graficamente precedente
	 */
	private int xGiocatore = 20;
	private int xAvversario = 20;
	
	/*
	 * I pannelli ove sistemare le carte dei rispettivi giocatori
	 */
	private PanelGioc avversario;
	private PanelGioc giocatore;

	private PanelImage contenitore;

	private PanelImage tavolo;
	
	// Il formato con cui si desidera visualizzare il valore numerico
	private final NumberFormat numberFormatter = NumberFormat.getNumberInstance(getLocale());
	
	private final static String VALUTA = "  Euro ";
	
	/**
	 * Costruisce la finestra principale che permette 
	 * l'interazione dell'utente con l'applicazione 
	 */
	public Gui(){
		super("Sette e Mezzo");
		
		// Imposta la finestra ridimensionabile.
		setResizable(true);
		
		/*
		 * Permettono di specificare la dimensione preferita della finestra,
		 * ma il calcolo effettivo della dimensione sara' delegato
		 * al gestore del layout della finestra.
		 */
		setPreferredSize(new Dimension(800,600));
		setMinimumSize(new Dimension(800,600));
		
		/*
		 *  Assegna le dimensioni della finestra 
		 *  e di tutto il suo contenuto secondo la gerarchia di contenimento.
		 *  Il metodo e' a disposizione solo per i contenitori top-level
		 */
		pack();
		
		// Imposta la posizione centrale del Frame
		setLocationRelativeTo(null);
		
		/*
		 *  Imposta il comportamento del programma 
		 *  in seguito all'evento chiusura della finestra.
		 */
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		/*
		 * Dal Class Object di questa classe (Gui),
		 * si ricava l'oggetto loader,
		 * responsabile del caricamento della medesima classe.
		 */
		ClassLoader loader = getClass().getClassLoader();
		
		/*
		 *  Dal loader si ricava il percorso assoluto dell'immagine 
		 *  a partire dal percorso relativo dato
		 */
		URL percorso = loader.getResource(IMG_PATH + "logo.png");
		
		/*
		 * Associazione di un'immagine fisica 
		 * alla variabile icon, dato un percorso 
		 */
		ICON = Toolkit.getDefaultToolkit().getImage(percorso);
		
		// Associazione dell'immagine creata come icona della finestra
		setIconImage(ICON);
		
		contenitore =  new PanelImage("background.png", new BorderLayout());
		add(contenitore);
		
		/*
		 * Creazione e aggiunta di pannelli vuoti per rendere i tavoli dei
		 * giocatori centrati
		 */
		JPanel vuoto = new JPanel();
		vuoto.setOpaque(false);
		
		JPanel vuoto1 = new JPanel();
		vuoto1.setOpaque(false);
		
		JPanel vuoto2 = new JPanel();
		vuoto2.setOpaque(false);
		
		JPanel vuoto3 = new JPanel();
		vuoto3.setOpaque(false);
		
		contenitore.add(vuoto,  BorderLayout.NORTH);
		contenitore.add(vuoto1, BorderLayout.WEST);
		contenitore.add(vuoto2, BorderLayout.EAST);
		contenitore.add(vuoto3, BorderLayout.SOUTH);
		
		/****** Creazione del Pannello contenente i giocatori ***********************************/
		
		tavolo = new PanelImage("panno.png", new GridLayout(2,1));
		contenitore.add(tavolo, BorderLayout.CENTER);
		
		avversario = new PanelGioc("Avversario", new String());
		tavolo.add(avversario);

		giocatore = new PanelGioc("Giocatore", GIOCATORE);
		tavolo.add(giocatore);

		// Rendo la finestra visibile
		setVisible(true);
		
		// Carico l'immagine del dorso della carta
		DORSO_CARTA = new JLabel(loadImage("00.png"));
	}
	
	@Override
	public String chiediTesto(String msg) {
		InputDialog input = new InputDialog(this, new DefaultCondition(), TITOLO, msg);
		
		// Viene restituito un input validato 
		return input.getValidatedText();
	}

	@Override
	public int chiediInteger(String msg) {
		InputDialog dialog = new InputDialog(this, new IntegerCondition(), TITOLO, msg);
		
		// Il testo validato viene parserizzato ad intero
		return Integer.parseInt(dialog.getValidatedText());
	}

	@Override
	public boolean chiediSiNo(String msg) {
		int risposta = JOptionPane.showConfirmDialog(this, msg, TITOLO, JOptionPane.YES_NO_OPTION);
		
		if(risposta == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	@Override
	public int videataIniziale() {
		/*
		 * Creazione di un array di oggetti indicanti 
		 * le scelte che un utente puo' effettuare
		 */
		Object[] opzioni = {"Storico Partite", "Gioca!", "Esci dal Gioco"};
	
		/*
		 * Quando l'utente clicchera' su uno dei bottoni 
		 * presenti nella finestra iniziale,
		 * la variabile decisione assumera' il valore dell'indice, che nell'array
		 * options, che punta all' opzione (botttone) scelto) 
		 */
		int decisione = JOptionPane.showOptionDialog(this,
				"Clicca sull'opzione desiderata!",
				TITOLO,
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opzioni,
				opzioni[1]);// La scelta di default e' quella posta in prima posizione
		 					// nell' array options
		
		return decisione;
	}

	@Override
	public String chiediData(){
		final JDialog richStorico = new JDialog(this, "Richiesta Data", true);
        richStorico.setModal(true);
        richStorico.setMinimumSize(new Dimension(500,300));
        richStorico.setLocationRelativeTo(this);
        
        JPanel panelCont = new JPanel(new BorderLayout());
		richStorico.setContentPane(panelCont);
       
		final String property = "sceltaGiorno";
        final Calendario calendario = new Calendario(2, property);
        calendario.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals(property))
                    richStorico.dispose();
            }
        });
        
        panelCont.add(calendario);   
     // Sul pannello appena creato si poggia un altro pannello...
		JPanel pan = new JPanel();
		
		// Creazione di un bordo senza contorno
		pan.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		
		//... che contiene il messaggio da visualizzare nel pannello
		pan.add(new JLabel("Clicca due volte sul giorno per il quale desideri visionare lo storico"));
		panelCont.add(pan, BorderLayout.NORTH);
		
		panelCont.add(new JPanel(), BorderLayout.EAST);
		panelCont.add(new JPanel(), BorderLayout.WEST);
		
        richStorico.pack();
        richStorico.setVisible(true);
        richStorico.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       
        return calendario.daiGiorno();
	}

	@Override
	public void setGiocatore(String nome, String ruolo, int credito, String tipoGioc){
		stampaNome(nome, tipoGioc);
		stampaRuolo(ruolo, tipoGioc);
		stampaCredito(credito, tipoGioc);
		
		DORSO_CARTA = new JLabel(loadImage("00.png"));
	}

	@Override
	public int chiediPuntata(int credito) {
		String msg = "Inserire un valore di puntata";

		/*
		 * JSlider e' un cursore a slitta, che permette di inserire valori numerici 
		 * compresi tra un massimo ed un minimo in maniera continua, 
		 * eliminando di fatto la possibilita' di inserire valori non corretti
		 */
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 1 , credito, 1);

		/*
		 *  Definisce l'intervallo numerico in cui devono essere
		 *  posizionate le suddivisioni rispettivamente grandi e 
		 *  piccole della scala graduata
		 */
		slider.setMajorTickSpacing(credito/5);
		slider.setMinorTickSpacing(credito/10);

		/*
		 * Indica che il cursore puo' accettare anche
		 * spostamenti non equivalenti alle posizioni che 
		 * si sono precedentemente impostati
		 */
		slider.setSnapToTicks(false);

		// Visualizza i numeri associati agli indicatori della scala graduata 
		slider.setPaintLabels(true);

		// Visualizza gli indicatori della scala graduata
		slider.setPaintTicks(true);

		InputDialog dialog = new InputDialog(this, slider, TITOLO, msg, 
				VALUTA, numberFormatter);

		/*
		 * Poiche' la formattazione prevedeva un punto ogni tre cifre, e'
		 * necessario eliminare tutti i punti prima della conversione in intero
		 */
		String valore = dialog.getValidatedText().replace(".", "");
		return Integer.parseInt(valore);
	}

	@Override
	public void stampaMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, TITOLO, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void stampaErr(String msg) {
		JLabel label = new JLabel("<html><b>" + msg + "</b></html>", SwingConstants.CENTER);
		label.setForeground(Color.red);
		JOptionPane.showMessageDialog(this, label, TITOLO, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void stampaStorico(ArrayList<PartitaEntry> storico){
		/*
		 * Creazione di un array di stringhe 
		 * rappresentanti i nomi delle colonne della tabella
		 */
		String[] nomiColonne = {
				"Nome giocatore", "Ruolo", "Punteggio",
				"Vincita", "Data"
			};
		
		/*
		 * Creazione di un array di array il cui numero di righe e' la dimensione
		 * dell'arraylist storico e il cui numero di colonne e' dato dalla 
		 * dimensione dell'array appena creato 
		 */
		Object[][] tabella = new Object[storico.size()][nomiColonne.length];
		
		/*
		 * Ogni elemento dell'arraylist storico viene convertito in array
		 */
		for(int i = 0; i < storico.size(); i++)
			tabella[i] = storico.get(i).toArray();  
		
		new TabellaStorico(this, "Storico Partite", nomiColonne, tabella);
	}

	@Override
	public void stampaNome(String nome, String tipoGioc){
		if(tipoGioc.equals(GIOCATORE))
		{
			// Si ricava il bordo del tavolo del Giocatore e si risetta il titolo
			TitledBorder border = (TitledBorder) giocatore.getBorder();
			border.setTitle(nome);
			
			// Aggiorna il look&Feed del tavolo del Giocatore Umano 
			SwingUtilities.updateComponentTreeUI(giocatore);
			border.setTitleColor(new Color(255, 178, 36));
		}
		else
		{
			// Si ricava il bordo del tavolo del Giocatore e si risetta il titolo
			TitledBorder border = (TitledBorder) avversario.getBorder();
			border.setTitle(nome);
			
			// Aggiorna il look&Feed del tavolo del Giocatore Umano 
			SwingUtilities.updateComponentTreeUI(avversario);
			border.setTitleColor(Color.white);
		}
	}

	@Override
	public void stampaRuolo(String ruolo, String tipoGioc){
		// Se l'utente e' giocatore Umano, stampa il ruolo nel tavolo in basso...
		if(tipoGioc.equals(GIOCATORE))
			giocatore.ruolo.setText(ruolo);
		else
			//... altrimenti nel tavolo in alto
			avversario.ruolo.setText(ruolo);
	}

	@Override
	public void stampaCredito(int credito, String tipoGioc) {
		String ammontare = numberFormatter.format(credito);
		
		if(tipoGioc.equals(GIOCATORE))
			giocatore.credito.setText(ammontare + VALUTA);
		else
			avversario.credito.setText(ammontare + VALUTA);
	}

	@Override
	public void stampaPuntata(int puntata, String tipoGioc) {
		String ammontare = numberFormatter.format(puntata);
		
		if(tipoGioc.equals(GIOCATORE))
			giocatore.puntata.setText( ammontare + VALUTA);
		else
			avversario.puntata.setText( ammontare + VALUTA);
	}

	@Override
	public void stampaCarta(String tipoGioc, ArrayList<Carta> carteSulTavolo) {
		// Si prende l'ultima carta distribuita al giocatore 
		Carta carta = carteSulTavolo.get(carteSulTavolo.size()-1);
		
		// Si associa alla stringa il nome dell'immagine della carta
		String labCarta = nomeImmagineCarta(carta);
		
		// Si associa l'immagine della carta con nome ricavato alla label
		JLabel imgCarta = new JLabel(loadImage(labCarta));
		
		// Se il giocatore in esame e' umano
		if(tipoGioc.equals(GIOCATORE))
		{
			/*
			 * Se e' la prima carta sistema con margine 20 dal bordo della finestra,
			 * altrimenti la sistema con margine 70
			 */
			if(carteSulTavolo.size()>1)
				xGiocatore += 70;
			
			// x, y, larghezza, altezza
			imgCarta.setBounds(xGiocatore, 45, 76, 120);
			
			// Viene aggiunta la carta sul layer in posizione 1 
			giocatore.tavolo.add(imgCarta, JLayeredPane.DEFAULT_LAYER);
		}
		else
		{
			/*
			 * Se e' un giocatore simulato (CPU)
			 * e non si deve stampare la carta coperta,
			 * stampa la carta come di default
			 */
			if(carteSulTavolo.size()>1)
			{
				xAvversario += 70;
				imgCarta.setBounds(xAvversario, 45, 76, 120);
				avversario.tavolo.add(imgCarta, JLayeredPane.DEFAULT_LAYER);
			}
			else
			{
				/*
				 * Se invece deve stampare la carta coperta,
				 * pone sul layer in posizione 0, il dorso della 
				 * carta ed in posizione -1, l'immagine frontale della carta
				 */
				DORSO_CARTA.setBounds(20, 45, 76, 120);
				avversario.tavolo.add(DORSO_CARTA, JLayeredPane.DEFAULT_LAYER);
				
				primaCartaCpu = imgCarta;
				imgCarta.setBounds(20, 45, 76, 120);
				avversario.tavolo.add(imgCarta, -1);
			}
		}
	}

	@Override
	public void stampaPunteggio(double punteggio, String tipoGioc){
		if(tipoGioc.equals(GIOCATORE))
			giocatore.punteggio.setText(Double.toString(punteggio));
		else
			avversario.punteggio.setText(Double.toString(punteggio));
	}

	@Override
	public void scopriCarta(Carta cartaCop, double punteggio, String tipoGioc) {
		/*
		 * L'immagine della carta coperta del banco deve essere resa visibile,
		 * quindi la si sposta sul layer in posizione 0, sopra il dorso
		 */
		avversario.tavolo.moveToFront(primaCartaCpu);
		stampaPunteggio(punteggio, tipoGioc);
	}

	@Override
	public void reset(){
		/*
		 * Vengono ricavati tutti i componenti presenti sui layer dedicati ai giocatori
		 * e successivamente rimossi
		 */
		Component[] carteCPU = avversario.tavolo.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER);
		for(Component carta : carteCPU)
			avversario.tavolo.remove(carta);
			
			
		
		Component[] carteUmano = giocatore.tavolo.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER);
		for(Component carta : carteUmano)
			giocatore.tavolo.remove(carta);
		
		 // Vengono resettati ai valori di default tutti i campi
		giocatore.puntata.setText("");
		giocatore.punteggio.setText("");
		avversario.puntata.setText("");
		avversario.punteggio.setText("");
		xGiocatore = 20;
		xAvversario = 20;

		// Viene aggiornato il look&Feel dei layer
		SwingUtilities.updateComponentTreeUI(avversario);
		SwingUtilities.updateComponentTreeUI(giocatore);
	}
	
	@Override
	public File carica() {
		JFileChooser fileChooser = new JFileChooser();
		
		// Viene aperta una finestra di dialogo per la selezione del file 
		int valoreRit = fileChooser.showOpenDialog(this);
		
		// Se l'utente clicca ok
		if(valoreRit == JFileChooser.APPROVE_OPTION)
		{
			// Creazione di un riferimento al file che l'utente ha selezionato
			File fileTemp = fileChooser.getSelectedFile();
			
			// Se il file esiste lo restituisce 
			if(fileTemp.exists())
				return fileTemp;
			else
			{
				/*
				 * Altrimenti notifica l'inesistenza del file e chiede se vuole
				 * caricare un altro file
				 */
				if(chiediSiNo("\n FILE INESISTENTE!!! \n Vuoi caricare un'altro file?"))
					return carica();
				else 
					return null;
			}
		}
		else
			return null;
	}

	@Override
	public File salva() {
		JFileChooser fileChooser = new JFileChooser();
		int valoreRit = fileChooser.showSaveDialog(this);
		
		if(valoreRit == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		else
			return null;
	}
	
	/*
	 * Recupera l'immagine con il nome passato come parametro
	 * @param name Il nome dell'icona da visualizzare
	 * @return L'immagine desiderata
	 */
	private ImageIcon loadImage(String name){
		ClassLoader loader = getClass().getClassLoader();
		URL percorso = loader.getResource(IMG_PATH + name);
		return new ImageIcon(percorso);
	}
	
	/*
	 * Restituisce il nome dell'immagine associata alla Carta passata come parametro
	 * @param carta La Carta di cui si desidera l'immagine relativa
	 * @return Una stringa che rappresenta il nome dell'immagine relativa
	 * 			in formato .png
	 */
	private String nomeImmagineCarta(Carta carta){
		return carta.daiTipoCarta().toString() + carta.daiSeme().toString() + ".png";
	}
	
	// Classe che costruisce un pannello con un' immagine di sfondo
	private class PanelImage extends JPanel{
	
		private static final long serialVersionUID = 3559310321717301074L;
		public ImageIcon sfondoPanel;
	
		/**
		 * Costruisce un pannello con un'immagine di sfondo e un layout
		 * @param image L'immagine di sfondo
		 * @param layout Il layout da associare al pannello
		 */
		public PanelImage(String image, LayoutManager layout) {
			sfondoPanel = loadImage(image);
			setLayout(layout);
			
			/*
			 *  Settando il pannello come non opaco, esso diviene trasparente,
			 *  prendendo dunque il background dell'immagine sottostante
			 */
			setOpaque(false);
		}
	
		public void paint(Graphics g) {
			sfondoPanel.paintIcon(this, g, 0, 0);
			super.paint(g);
		}
	
		public Dimension getPreferredSize() {
			return new Dimension(sfondoPanel.getIconWidth(),
					sfondoPanel.getIconHeight());
		}
	}
	
	/*
	 * Classe che costruisce il pannello ove poggiare le carte del giocatore.
	 * se il giocatore e' l'avversario i campi relativi al ruolo, puntata, punteggio
	 * e credito saranno di colore arancio scuro, 
	 * altrimenti saranno di colore bianco
	 */
	private class PanelGioc extends JPanel{
		
		private static final long serialVersionUID = -7154190285186445181L;
		public JLayeredPane tavolo;
		public JLabel ruolo;
		public JLabel punteggio;
		public JLabel puntata;
		public JLabel credito;
	
		/**
		 * Costruisce un in base al tipo di giocatore
		 * @param titleBorder Il titolo da inserire nel bordo del pannello
		 * @param tipoGioc Il tipo del giocatore in esame: 
		 * <code>GIOCATORE</code> oppure <code>AVVERSARIO</code>
		 */
		public PanelGioc(String titleBorder, String tipoGioc){
	
			setLayout(new GridBagLayout());
			setOpaque(false);
			
			/*
			 * yLayer e yCampi servono a sistemare i campi relativi al giocatore:
			 * in alto se e' avversario, in basso altrimenti.
			 * Cio' serve per evitare che le finestre modali si sovrappongano 
			 * ai campi di ciascun utente, utili per avere una visione istantanea
			 * e chiara della partita
			 */
			int yLayer = 0, yCampi = 0;
			
			/*
			 * weightyCampi affinche' i campi del giocatore 
			 * non escano fuori dal bordo della finestra
			 */
			double weightyCampi = 0.0;
			
			// Il colore varia in base al tipo di giocatore
			Color colore;
			
			/* 
			 * Serve per diversificare la spaziatura tra le carte e il pannello
			 * contenente i campi del giocatore, poiche' la posizione di quest'ultimo
			 * varia in base al tipo di giocatore
			 */
			int bottom;
			
			if(tipoGioc.equalsIgnoreCase(GIOCATORE))
			{
				yCampi = 1;
				weightyCampi = 0.2;
				colore = new Color(255, 178, 36);
				bottom = -20;
			}
			else
			{	
				yLayer = 1;
				colore = Color.white;
				bottom = -10;
			}
	
			/***** Creazione del Pannello contenente il tavolo dello sfidante ***********************************/		
	
			// Creazione del pannello contenente il tavolo del giocatore
			tavolo = new JLayeredPane();
	
			JPanel campi = new JPanel();
			campi.setOpaque(false);
	
			// Pannello contenente i campi relativi al ruolo
			JPanel uno = new JPanel();
			uno.setOpaque(false);
			JLabel lRuolo = new JLabel("<html><b><i><u>Ruolo:</u>     </i></b></html>");
			lRuolo.setForeground(colore);
			
			ruolo = new JLabel();
			ruolo.setForeground(colore);
			
			uno.add(lRuolo);
			uno.add(ruolo);
			
			// Label vuota per fare spazio tra un pannello e l'altro
			uno.add(new JLabel("  "));
			
			// Pannello contenente i campi relativi al punteggio
			JPanel due = new JPanel();
			due.setOpaque(false);
			JLabel lPunteggio = new JLabel("<html><b><i><u>Punteggio:</u>     </i></b></html>");
			lPunteggio.setForeground(colore);
			
			punteggio = new JLabel();
			punteggio.setForeground(colore);
			
			due.add(lPunteggio);
			due.add(punteggio);
			due.add(new JLabel("    "));
			
			// Pannello contenente i campi relativi alla puntata
			JPanel tre = new JPanel();
			tre.setOpaque(false);
			JLabel lPuntata = new JLabel("<html><b><i><u>Puntata:</u>     </i></b></html>");
			lPuntata.setForeground(colore);
			
			puntata = new JLabel("        ");
			puntata.setForeground(colore);
			
			tre.add(lPuntata);
			tre.add(puntata);
			tre.add(new JLabel("    "));
			
			// Pannello contenente i campi relativi al credito
			JPanel quattro = new JPanel();
			quattro.setOpaque(false);
			JLabel lCredito = new JLabel("<html><b><i><u>Credito:</u>     </i></b></html>");
			lCredito.setForeground(colore);
			
			credito = new JLabel();
			credito.setForeground(colore);
			
			quattro.add(lCredito);
			quattro.add(credito);
			quattro.add(new JLabel("    "));
	
			campi.add(uno);
			campi.add(due);
			campi.add(tre);
			campi.add(quattro);
			
			// Creazione del bordo per il pannello contenente i campi
			campi.setBorder(BorderFactory.createEmptyBorder(0, 0, bottom, 0));
			
			/****** Sistemazione del Pannello per il tavolo dello sfidante ***********/	      
	
			GridBagConstraints rigaLayer = new GridBagConstraints();
			rigaLayer.gridx = 0;
			rigaLayer.gridy = yLayer;
			rigaLayer.weightx = 1.0;
			rigaLayer.weighty = 1.0;
			rigaLayer.fill = GridBagConstraints.BOTH;
			add(tavolo, rigaLayer);
	
			/****************** Sistemazione pannello dei campi utente****************/	   	
	
			/*
			 * GridBagConstraints e' una classe utilizzata per specificare i vincoli che
			 * l'elemento aggiunto al contenitore, con GridBagLayout, deve rispettare 
			 */
			GridBagConstraints rigaCampi = new GridBagConstraints();
			
			// riga
			rigaCampi.gridx = 0;
			
			// colonna
			rigaCampi.gridy = yCampi;
			
			rigaCampi.weighty = weightyCampi;
			
			/*
			 * l'elemento deve variare proporzionalmente 
			 * sia in orizzontale che verticale
			 */
			rigaCampi.fill = GridBagConstraints.BOTH;
			add(campi, rigaCampi);
	
			// Crea un bordo con effetto "inciso" e un titolo (come fieldset in html)
			setBorder(
					new TitledBorder(
							new EtchedBorder(colore, colore), titleBorder,
							TitledBorder.DEFAULT_JUSTIFICATION,
							TitledBorder.DEFAULT_POSITION,
							null, null));
		}
	}
}