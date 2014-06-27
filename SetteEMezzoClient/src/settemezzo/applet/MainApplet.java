package settemezzo.applet;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import settemezzo.applet.util.*;
import settemezzo.applet.componenti.InputDialog;
import settemezzo.applet.componenti.calendario.*;
import settemezzo.applet.componenti.help.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp MainApplet</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe i cui oggetti sono
 * seguiti nel contesto di un altro programma su di un computer server. 
 * Gli oggetti di tali classi sono dotati di interfaccia utente
 * grafica, che consente al quest'ultimo di interagire con l'applicazione
 * mediante finestre, icone, menu e dispositivo di puntamento (Interazione WIMP)  </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class MainApplet extends JApplet {
	public final static String GIOCATORE = "giocatore";

	// Il JDialog adibito ai credits
	JDialog infoDialog;

	// Menu' per modificare l'aspetto dell'applicazione
	JMenu menuAspetto;

	// Il Frame adibito alla guida in linea
	HelpFrame help;

	private static final long serialVersionUID = 1337282728608396825L;

	// Il formato con cui si desidera visualizzare il valore numerico
	private final NumberFormat numberFormatter = NumberFormat.getNumberInstance(getLocale());

	private final static String TITOLO = "Sette e Mezzo";
	private final static String VALUTA = "  Euro ";
	private final static String IMG_PATH = "images/";
	private final static String HTML_PATH = "html/";
	
	private JLabel DORSO_CARTA = new JLabel();
	
	/* 
	 * La barra del menu che consente all'utente 
	 * di accedere alle funzioni dell'applicazione
	 */
	private JMenuBar menu;
	
	/*
	 * Il menu' che consente di avviare,
	 * salvare e caricare una partita, 
	 * visionare lo storico, registrarsi ed autenticarsi
	 */
	private JMenu menuPartita;
	private JMenuItem nuovaPartita;
	private JMenuItem caricaPartita;
	private JMenuItem salvaPartita;
	private JMenuItem loginPartita;
	private JMenuItem registrazione;
	
	// Il pannello ove risiedono i bottoni con il quale l'utente interagisce col gioco
	private JPanel bottGiocatore;
	
	// Pulsanti di interazione dell'utente
	private JButton punta;
	private JButton altraCarta;
	private JButton stai;
	private JButton altraMano;
	
	/*
	 * La distanza che le carte nei vari Layer devono avere 
	 * dall'elemento graficamente precedente
	 */
	private int xGiocatore = 20;
	private int xAvversario = 20;
	
	// La carta coperta distribuita all'avversario
	private JLabel primaCartaAvversario;
	
	// Gestore degli eventi dell'interfaccia
	private Mediatore evento;
	
	/*
	 *  Il pannello principale che contiene tutti gli elementi
	 *  costituenti l'interfaccia 
	 */
	private JPanel contenitore;
	
	// Il pannello rappresentante il tavolo ove giocano i giocatori
	private JPanel tavolo;
	private PanelGioc avversario;
	private PanelGioc giocatore;
	
	// Il logo dell'applicazione
	private JLabel logo;
	
	// Le informazioni per iniziare la partita
	private JLabel info;
	
	/**
	 * Crea lo scheletro dell'interfaccia e fa avviare l'applet
	 */
	public void init() {
		setSize(new Dimension(800,600));
		
		evento = new Mediatore(this, getParameter("porta"));
//		evento = new Mediatore(this, "6502");
		
		// Creo una barra a cui verranno associati i menu'
		menu = new JMenuBar();
		setJMenuBar(menu);

		/***** Menu Partita ***********************************************************/

		menuPartita = newMenu("Partita", KeyEvent.VK_P, menu);

		nuovaPartita = newMenuItem("Nuova partita   ", 
				KeyEvent.VK_N, ActionEvent.CTRL_MASK, menuPartita, evento);

		caricaPartita = newMenuItem("Carica partita   ",
				KeyEvent.VK_O, ActionEvent.CTRL_MASK, menuPartita, evento);

		salvaPartita = newMenuItem("Salva partita   ",
				KeyEvent.VK_S, ActionEvent.CTRL_MASK, menuPartita, evento);

		salvaPartita.setEnabled(false);

		menuPartita.addSeparator();
		
		loginPartita = newMenuItem("Login   ",
				KeyEvent.VK_L, ActionEvent.CTRL_MASK, menuPartita, evento);
		
		registrazione = newMenuItem("Registrazione   ",
				KeyEvent.VK_R, ActionEvent.CTRL_MASK, menuPartita, evento);

		menuPartita.addSeparator();

		newMenuItem("Storico   ",
				KeyEvent.VK_T, ActionEvent.CTRL_MASK, menuPartita, evento);


		/***** Menu Aspetto ***********************************************************/		

		 menuAspetto = newMenu("Aspetto", KeyEvent.VK_E, menu);
		
		/*
		 * Calcola i look and feel installati nella macchina su cui
		 * viene lanciata l'applicazione
		 */
		UIManager.LookAndFeelInfo[] lnfSystem = UIManager.getInstalledLookAndFeels();
		
		/*
		 * Crea il gruppo di bottoni
		 */
		ButtonGroup lnfSystemGroup = new ButtonGroup();
		
		// Per ciascun tema installato
		for (int i = 0; i < lnfSystem.length; i++) 
		{
			// Se e' diverso dal Motif(brutto)
			if (!lnfSystem[i].getName().equalsIgnoreCase("CDE/Motif")) 
			{
				// Crea un JRadioButton specifico per i menu
				JRadioButtonMenuItem radioButtonMi = new JRadioButtonMenuItem(
															lnfSystem[i].getName());
				
				// Lo aggiunge al menu aspetto
				menuAspetto.add(radioButtonMi);

				// Seleziona il tema creato
				radioButtonMi.setSelected(
						UIManager.getLookAndFeel().getName().equalsIgnoreCase(lnfSystem[i].getName()));

				/*
				 * Viene associato a questo bottone una proprieta' con chiave 
				 * "nomeLookAndFeel" e valore dato dall'indice del tema analizzato 
				 * per riferirsi ad esso
				 */
				radioButtonMi.putClientProperty("nomeLookAndFeel", lnfSystem[i]);

				// Viene associato ad esso un evento
				radioButtonMi.addItemListener(evento);
				
				/*
				 * Viene aggiunto il radioButton appena creato
				 * al gruppo di bottoni del menu 
				 */
				lnfSystemGroup.add(radioButtonMi);
			}
		}

		/***** Menu Aiuto ************************************************************/

		JMenu menuAiuto = newMenu("Aiuto", KeyEvent.VK_I, menu);

		newMenuItem("Guida   ",
				KeyEvent.VK_F1, 0, menuAiuto, evento);

		newMenuItem("Informazioni   ", menuAiuto, evento);
		
		
		// Creazione della schermata iniziale
		contenitore =  new PanelImage("background.png", new BorderLayout());

		logo = new JLabel(loadImage("logo.png"), SwingConstants.CENTER);
		contenitore.add(logo, BorderLayout.CENTER);
		
		info = new JLabel("Per giocare seleziona dal menu Partita la voce Nuova Partita", SwingConstants.CENTER);
		info.setForeground(Color.white);
		info.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		Font font = info.getFont();
		info.setFont(new Font(Font.DIALOG, font.getStyle(), 20));
		contenitore.add(info, BorderLayout.SOUTH);
		
		add(contenitore);

		// Rendo la finestra visibile
		setVisible(true);

		// Carico l'immagine del dorso della carta
		DORSO_CARTA = new JLabel(loadImage("00.png"));
	}
	
	/**
	 * Crea il tavolo da gioco
	 */
	public void creaTavolo(){
		// Rimuove tutti i componenti precedentemente sistemati sul pannello contenitore
		contenitore.removeAll();
		
		tavolo = new PanelImage("panno.png", new GridLayout(2,1));
		
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
		
		contenitore.add(tavolo, BorderLayout.CENTER);
		contenitore.add(vuoto1, BorderLayout.WEST);
		contenitore.add(vuoto2, BorderLayout.EAST);
		contenitore.add(vuoto, BorderLayout.NORTH);
		
		/****** Creazione del Pannello contenente bottoni *****************************/
		
		// Creazione del pannello contenente i bottoni di interazione
		bottGiocatore = new JPanel();
		// Impostazione della trasparenza al pannello e creazione di un bordo non visibile
		bottGiocatore.setOpaque(false);
		bottGiocatore.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contenitore.add(bottGiocatore, BorderLayout.SOUTH);
		
		SwingUtilities.updateComponentTreeUI(contenitore);
		
		// Creazione del pulsante Punta
		punta = creaBottone(" Punta ", "gray.png", "orange.png", evento, false, Color.darkGray);
		punta.addKeyListener(evento);
		bottGiocatore.add(punta);

		// Creazione del pulsante Altra Carta
		altraCarta = creaBottone(" Altra Carta ", "gray.png", "orange.png", evento, false, Color.darkGray);
		altraCarta.addKeyListener(evento);
		bottGiocatore.add(altraCarta);
		
		// Creazione del pulsante Stai
		stai = creaBottone(" Stai ", "gray.png", "orange.png", evento, false, Color.darkGray);
		stai.addKeyListener(evento);
		bottGiocatore.add(stai);

		// Creazione del pulsante Altra Mano
		altraMano = creaBottone(" Altra Mano ", "gray.png", "orange.png", evento, false, Color.darkGray);
		altraMano.addKeyListener(evento);
		bottGiocatore.add(altraMano);

		// Creazione del pannello dei due giocatori
		avversario = new PanelGioc("Avversario", new String());
		tavolo.add(avversario);

		giocatore = new PanelGioc("Giocatore", GIOCATORE);
		tavolo.add(giocatore);
	}
	
	/**
	 * Richiede all'utente quale partita caricare tra quelle proposte
	 * @param salvataggi lista delle partite salvate
	 * @return la partita da caricare selezionata dall'utente
	 */
	public String chiediSalvataggio(Object[] salvataggi) {
		 InputDialog dialog = new InputDialog(this, TITOLO, salvataggi, "Desideri caricare la partita del: ");
		 return dialog.getValidatedText();
	}

	/**
	 * Richiede le credenziali di accesso ai servizi aggiuntivi offerti dall'applicazione
	 * @param msg Il messaggio da visualizzare nella finestra di autenticazione
	 * @return le credenziali d'accesso del giocatore
	 */
	public String[] login(String msg) {
		InputDialog login = new InputDialog(this, new DefaultCondition(), TITOLO, msg, "Email: ", "Password: ");
		return login.getValidatedTexts();
	}

	/**
	 * Visualizza una domanda e richiede per essa
	 * una risposta di tipo testuale
	 * @param msg La domanda da visualizzare
	 * @return La stringa digitata dall'utente
	 */
	public String chiediTesto(String msg) {
		InputDialog input = new InputDialog(this, new DefaultCondition(), TITOLO, msg);

		// Viene restituito un input validato 
		return input.getValidatedText();
	}
	
	/**
	 * Visualizza un messaggio 
	 * @param msg Il messaggio da visualizzare
	 */
	public void stampaMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, TITOLO, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Visualizza un messaggio di errore 
	 * @param msg Il messaggio d'errore da visualizzare
	 */
	public void stampaErr(String msg1, String msg2) {
		JLabel labelErr1 = new JLabel("<html><b>" + msg1 + "</b></html>", SwingConstants.CENTER);
		JLabel labelErr2 = new JLabel("<html><b>" + msg2 + "</b></html>", SwingConstants.CENTER);
		labelErr1.setForeground(Color.red);
		labelErr2.setForeground(Color.red);
		
		JLabel[] label = {labelErr1, labelErr2};
		
		JOptionPane.showMessageDialog(this, label, TITOLO, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Restituisce il nome del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame: 
	 * <code>GIOCATORE</code> oppure <code>AVVERSARIO</code> 
	 * @return il nome del giocatore in esame
	 */
	public String daiNome(String tipoGioc){
		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
		{
			// Si ricava il bordo del tavolo del Giocatore e si risetta il titolo
			TitledBorder border = (TitledBorder) giocatore.getBorder();
			return border.getTitle();
		}
		else // tipoGioc e' uguale ad AVVERSARIO
		{
			TitledBorder border = (TitledBorder) avversario.getBorder();
			return border.getTitle();
		}
	}

	/**
	 * Visualizza il nome del giocatore in esame
	 * @param nome Il nome del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame:  
	 * <code>GIOCATORE</code> oppure <code>AVVERSARIO</code> 
	 */
	public void stampaNome(String nome, String tipoGioc){
		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
		{
			// Si ricava il bordo del tavolo del Giocatore e si reimposta il titolo
			TitledBorder border = (TitledBorder) giocatore.getBorder();
			border.setTitle(nome);
			border.setTitleColor(new Color(255, 178, 36));

			// Aggiorna il tavolo del Giocatore  
			SwingUtilities.updateComponentTreeUI(giocatore);
		}
		else // tipoGioc e' uguale ad AVVERSARIO
		{
			TitledBorder border = (TitledBorder) avversario.getBorder();
			border.setTitle(nome);
			SwingUtilities.updateComponentTreeUI(avversario);
			border.setTitleColor(Color.white);
		}
	}
	
	/**
	 * Restituisce il ruolo dell'utente
	 * @return il ruolo del giocatore
	 */
	public String daiRuolo() {
		return giocatore.ruolo.getText();
	}

	/**
	 * Visualizza il ruolo del giocatore in esame
	 * @param ruolo Il ruolo del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code> 
	 */
	public void stampaRuolo(String ruolo, String tipoGioc) {
		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
			giocatore.ruolo.setText(ruolo);
		else
			avversario.ruolo.setText(ruolo);
	}
	
	/**
	 * Visualizza il credito del giocatore in esame
	 * @param credito Il credito da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code> 
	 */
	public void stampaCredito(int credito, String tipoGioc) {
		String ammontare = numberFormatter.format(credito);
		
		if(tipoGioc.equals(GIOCATORE))
			giocatore.credito.setText( ammontare + VALUTA);
		else
			avversario.credito.setText( ammontare + VALUTA);
	}
	
	/**
	 * Visualizza il punteggio del giocatore in esame
	 * @param punteggio Il punteggio del giocatore in esame
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code> 
	 */
	public void stampaPunteggio(String punteggio, String tipoGioc){
		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
			giocatore.punteggio.setText(punteggio);
		else
			avversario.punteggio.setText(punteggio);
	}

	/**
	 * Richiede una puntata
	 * @return La puntata del giocatore
	 */
	public int chiediPuntata() {
		String msg = "Inserire un valore di puntata";

		/*
		 * JSlider e' un cursore a slitta, che permette di inserire valori numerici 
		 * compresi tra un massimo ed un minimo in maniera continua, 
		 * eliminando di fatto la possibilita' di inserire valori non corretti
		 */
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 1 , daiCredito(), 1);

		/*
		 *  Definisce l'intervallo numerico in cui devono essere
		 *  posizionate le suddivisioni rispettivamente grandi e 
		 *  piccole della scala graduata
		 */
		slider.setMajorTickSpacing(daiCredito()/5);
		slider.setMinorTickSpacing(daiCredito()/10);

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

	/**
	 * Visualizza la puntata del giocatore in esame
	 * @param puntata La puntata da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code>  
	 */
	public void stampaPuntata(int puntata, String tipoGioc) {
		// Formattazione della puntata
		String ammontare = numberFormatter.format(puntata);
		
		if(tipoGioc.equals(GIOCATORE))
			giocatore.puntata.setText( ammontare + VALUTA);
		else
			avversario.puntata.setText( ammontare + VALUTA);
	}

	/**
	 * Visualizza la prima carta distribuita al giocatore in esame.
	 * Se il giocatore e' l'avversario ne visualizza il dorso, altrimenti
	 * la visualizza normalmente 
	 * @param seme Il seme della carta da visualizzare
	 * @param tipoCarta Il tipo della carta da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code> 
	 */
	public void stampaCartaCoperta(String seme, String tipoCarta, String tipoGioc) {

		// Si associa l'immagine della carta con nome ricavato alla label
		JLabel imgCarta = new JLabel(loadImage(tipoCarta + seme + ".png"));

		// Se il giocatore in esame e' il giocatore
		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
		{
			// x, y, larghezza, altezza
			imgCarta.setBounds(xGiocatore, 30, 76, 120);

			// Viene aggiunta la carta sul layer in posizione 1 
			giocatore.tavolo.add(imgCarta, JLayeredPane.DEFAULT_LAYER);
		}
		else
		{
			/*
			 * Se invece deve stampare la carta coperta,
			 * pone sul layer in posizione 0, il dorso della 
			 * carta ed in posizione -1, l'immagine frontale della carta
			 */
			DORSO_CARTA.setBounds(20, 30, 76, 120);
			avversario.tavolo.add(DORSO_CARTA, JLayeredPane.DEFAULT_LAYER);

			primaCartaAvversario = imgCarta;
			imgCarta.setBounds(20, 30, 76, 120);
			avversario.tavolo.add(imgCarta, -1);
		}
	}
	
	/**
	 * Visualizza la carta passata come parametro al giocatore in esame
	 * @param seme Il seme della carta da visualizzare
	 * @param tipoCarta Il tipo della carta da visualizzare
	 * @param tipoGioc Il tipo del giocatore in esame:
	 * <code> GIOCATORE </code> oppure <code> AVVERSARIO </code> 
	 */
	public void stampaCarta(String seme, String tipoCarta, String tipoGioc) {
		// Si associa l'immagine della carta alla label
		JLabel imgCarta = new JLabel(loadImage(tipoCarta + seme + ".png"));

		if(tipoGioc.equalsIgnoreCase(GIOCATORE))
		{
			xGiocatore += 70;

			// ascissa (x), ordinata (y), larghezza, altezza
			imgCarta.setBounds(xGiocatore, 30, 76, 120);

			// Viene aggiunta la carta sul layer in posizione 1 
			giocatore.tavolo.add(imgCarta, JLayeredPane.DEFAULT_LAYER);
		}
		else
		{
			xAvversario += 70;
			
			// ascissa (x), ordinata (y), larghezza, altezza
			imgCarta.setBounds(xAvversario, 30, 76, 120);
			
			avversario.tavolo.add(imgCarta, JLayeredPane.DEFAULT_LAYER);
		}
	}
	
	/**
	 * Scopre la carta coperta dell'avversario
	 */
	public void scopriCarta() {
		/*
		 * L'immagine della carta coperta del banco deve essere resa visibile,
		 * quindi la si sposta sul layer in posizione 0, sopra il dorso
		 */
		avversario.tavolo.moveToFront(primaCartaAvversario);				
	}
	
	/**
	 * Resetta l'interfaccia utente, rimuovendo le carte distribuite
	 */
	public void resetTavoli() {
		/*
		 * Vengono ricavate tutte le carte presenti sui layer dedicati ai giocatori
		 * e successivamente rimosse
		 */
		Component[] carteAvversario = avversario.tavolo.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER);
		for(Component carta : carteAvversario)
			avversario.tavolo.remove(carta);

		Component[] carteGiocatore = giocatore.tavolo.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER);
		for(Component carta : carteGiocatore)
			giocatore.tavolo.remove(carta);

		// Vengono aggiornati i layer
		SwingUtilities.updateComponentTreeUI(avversario.tavolo);
		SwingUtilities.updateComponentTreeUI(giocatore.tavolo);
	}
	
	/**
	 * Resetta i campi relativi al giocatore
	 */
	public void resetCampi() {
		// Vengono resettati ai valori di default tutti i campi
		giocatore.puntata.setText("");
		giocatore.punteggio.setText("");
		avversario.puntata.setText("");
		avversario.punteggio.setText("");
		xGiocatore = 20;
		xAvversario = 20;
	}

	/**
	 * Presenta un calendario modale per la richiesta di una data
	 * @return la data scelta dall'utente nel formato "yyyy-MM-dd"
	 */
	public String chiediData() {
		// Creazione di un JDialog...
		final JDialog richStorico = new JDialog();
		richStorico.setModal(true);
		richStorico.setTitle("Richiesta Data");
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
	
	
	/**
	 * Crea un finestra modale che mostra i credits del programma
	 */
	public void informazioni() {
		infoDialog = new JDialog();
		
		infoDialog.setResizable(false);
		infoDialog.setModal(true);
		infoDialog.setTitle(TITOLO);
		
		// Creazione di un pannello contenente l'immagine dei credits
		JPanel panelInfo = new PanelImage("About.png", new BorderLayout());
		
		infoDialog.add(panelInfo, BorderLayout.CENTER);
		
		// Pannello contenente il bottone "OK"
		JPanel panelButton = new JPanel();
		panelButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	    panelButton.setOpaque(false);
	    
	    // Creazione del bottone "OK" 
	    JButton button = (JButton) panelButton.add(new JButton("OK"));
	    button.setMnemonic(KeyEvent.VK_O);
	    button.addKeyListener(evento);
	    button.addActionListener(evento);
	    
	    panelInfo.add(panelButton, BorderLayout.SOUTH);
	    
	    infoDialog.pack();
	    
	    infoDialog.setLocationRelativeTo(getRootPane());
	    
	    infoDialog.setVisible(true);
	}

	/**
	 * Visualizza la guida in linea dell'applicazione
	 */
	public void guida() {
		Image icon = loadImage("logo.png").getImage();
		help = new HelpFrame("Guida in linea", icon, creaStruttura());		
	}
	
	/**
	 * Imposta il testo dei bottoni a seconda se sono selezionati o meno
	 * @param button Il buttone di cui bisogna reimpostare il testo 
	 * @param focusOn true se ha il focus, false se l'ha perso
	 */
	public void buttonFocusOn(JButton button, boolean focusOn){
		if(focusOn)
			button.setForeground(Color.white);
		else
			button.setForeground(Color.darkGray);
	}

	/**
	 * Abilita\Disabilita il pulsante con etichetta "Login"
	 * @param abilita Abilita il pulsante se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaLogin(boolean abilita){
		loginPartita.setEnabled(abilita);
	}
	
	/**
	 * Abilita\Disabilita il pulsante con etichetta "Registrazione"
	 * @param abilita Abilita il pulsante se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaRegistrazione(boolean abilita){
		registrazione.setEnabled(abilita);
	}
	
	/**
	 * Abilita\Disabilita il pulsante con etichetta "Puntata"
	 * @param abilita Abilita il pulsante se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaPunta(boolean abilita){
		punta.setEnabled(abilita);
		
		if(abilita)
			punta.requestFocus();
	}

	/**
	 * Abilita\Disabilita il pulsante con etichetta "Altra Carta"
	 * @param abilita Abilita il pulsante se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaAltraCarta(boolean abilita){
		altraCarta.setEnabled(abilita);
		stai.setEnabled(abilita);
		
		if(abilita)
			altraCarta.requestFocus();
	}
	
	/**
	 * Abilita\Disabilita il pulsante con etichetta "Altra Mano" 
	 * @param abilita Abilita tale pulsante se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaAltraMano(boolean abilita){
		altraMano.setEnabled(abilita);
		if(abilita)
			altraMano.requestFocus();
	}
	
	/**
	 * Abilita\Disabilita la voce con etichetta "Nuova Partita" dal menu Partita
	 * @param abilita Abilita tale voce se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaNuovaPartita(boolean abilita){
		nuovaPartita.setEnabled(abilita);
	}
	
	/**
	 * Abilita\Disabilita la voce con etichetta "Carica Partita" dal menu Partita
	 * @param abilita Abilita tale voce se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaCaricaPartita(boolean abilita){
		caricaPartita.setEnabled(abilita);
	}
	
	/**
	 * Abilita\Disabilita la voce con etichetta "Salva Partita" dal menu Partita 
	 * @param abilita Abilita tale voce se e' uguale a true, disabilita altrimenti
	 */
	public void abilitaSalvaPartita(boolean abilita){
		salvaPartita.setEnabled(abilita);
	}

	@Override
	public void destroy(){
		evento.finePartita();
		super.destroy();
	}

	/*
	 * Restituisce il credito del giocatore presente nell'interfaccia
	 * @return il credito del giocatore
	 */
	private int daiCredito() {
		String credito = giocatore.credito.getText();
		credito = credito.substring(0, credito.length() - VALUTA.length());
		credito = credito.replace(".", "");
		return Integer.parseInt(credito);
	}

	/*
	 * Recupera l'immagine con il nome passato come parametro
	 * @param name Il nome dell'icona da visualizzare
	 * @return L'immagine desiderata
	 */
	private ImageIcon loadImage(String name) {
		ClassLoader loader = getClass().getClassLoader();
		URL percorso = loader.getResource(IMG_PATH + name);
		return new ImageIcon(percorso);
	}

	/*
	 * Crea un menu'
	 * @param nomeMenu Testo da associato al menu'
	 * @param mnemonic Combinazione di tasti per la scelta rapida
	 * @param bar barra dei menu' a cui associare il menu' 
	 * @return Il menu creato
	 */
	private JMenu newMenu(String nomeMenu, int mnemonic, JMenuBar bar) {
		JMenu menu = new JMenu(nomeMenu);

		menu.setMnemonic(mnemonic);
		/* 
		 * setMnemonic permette di impostare una shortcut
		 *  per gli elementi visibili di un menu'.
		 */

		bar.add(menu);
		return menu;		
	}
	
	/*
	 * Crea un sottomenu' e lo aggiunge ad un menu'
	 * @param nomeItem Testo da associato al sottomenu'
	 * @param menu Menu' a cui associare il sottomenu' creato
	 * @param listener Listener associato all'evento "elemento selezionato"
	 * @return il JMenuItem creato
	 */
	private JMenuItem newMenuItem(String nomeItem, JMenu menu, ActionListener listener) {
		JMenuItem item = new JMenuItem(nomeItem);
		
		// Aggiuge listener all'item
		item.addActionListener(listener);
		menu.add(item);
		return item;
	}

	/*
	 * Crea un sottomenu' e lo aggiunge ad un menu' 
	 * @param nomeItem Testo associato al sottomenu'
	 * @param mnemonic Combinazione di tasti per la scelta rapida
	 * @param menu Menu' a cui associare il sottomenu' creato
	 * @param listener Listener associato all'evento "elemento selezionato"
	 * @return il JMenuItem creato 
	 */
	private JMenuItem newMenuItem(String nomeItem, int tasto, int mask, JMenu menu, ActionListener listener){
		JMenuItem menuItem = newMenuItem(nomeItem, menu, listener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(tasto,mask));
		return menuItem;
	}
	
	/*
	 * Crea bottoni con immagini ed eventi personalizati
	 * @param nomeBottone L'etichetta del pulsante
	 * @param nomeimg1 Il nome dell'immagine del bottone
	 * @param nomeimg2 Il nome dell'immagine del bottone quando viene premuto
	 * @param eventListner L'evento associato al bottone
	 * @param colore Il colore del testo del bottone
	 * @return il bottone creato
	 */
	private JButton creaBottone(String nomeBottone, String nomeimg1, String nomeimg2, 
									ActionListener eventListner, boolean abilitato, Color colore){
		// Crea il bottone associandogli l'etichetta e l'immagine 
		JButton bottone = new JButton(nomeBottone , loadImage(nomeimg1));
		
		// Si setta lo sfondo del bottone trasbarente
		bottone.setOpaque(false);
		
		bottone.setForeground(colore);
		
		// Si setta il bottone java come non visibile
		bottone.setContentAreaFilled(false);
		
		// Si setta il contorno interno come non visibile
		bottone.setFocusPainted(false);
		
		// Si setta il bordo del bottone come non visibile
		bottone.setBorderPainted(false);
		
		// Si setta l'immagine da visualizzare quando il pulsante viene premuto
		bottone.setPressedIcon(loadImage(nomeimg2));
		
		/*
		 * Si setta l'immagine da visualizzare quando il cursore del mouse
		 * oltrepassa l'area assegnata al bottone
		 */
		bottone.setRolloverIcon(loadImage(nomeimg2));
		
		bottone.addFocusListener(evento);
		
		// Setto la posizione dell'etichetta nell'immagine in questo caso centrale
		bottone.setVerticalTextPosition(AbstractButton.CENTER);
		bottone.setHorizontalTextPosition(AbstractButton.CENTER);
		
		// Aggiunta di un evento al pulsante
		bottone.addActionListener(eventListner);
		
		bottone.setEnabled(abilitato);
		
		return bottone;
	}
	
	/*
	 * Crea la struttura ad albero dell'indice dell'help
	 * @return Il nodo radice dell'indice
	 */
	private Nodo creaStruttura() {
	    DefaultMutableTreeNode nodoInt = null;
	    Nodo foglia = null;
	
	    /*
	     * La struttura dell'indice creata con questo metodo e' la seguente
	     * 
	     * Guida al sette e mezzo
	     *  |
	     *  +-- Il gioco del Sette e Mezzo
	     *  |	|
	     *  |	+-- Basi del gioco del Sette e Mezzo
	     *  |	|
	     *  |	+-- Cenni preliminari
	     *  |	|
	     *  |	+-- Svolgimento del gioco
	     *  |
	     *  +-- Iniziamo a giocare
	     * 	|	|
	     * 	|	+-- Avviare SetteEMezzo
	     * 	|	|
	     * 	|	+-- Barra dei menu'
	     * 	|   |
	     * 	|	+-- Area di gioco
	     * 	|
	     *  +-- Opzioni per i registrati
	     * 		|
	     * 		+-- Come registrarsi
	     * 		|
	     * 		+-- Come autenticarsi
	     * 	    |
	     * 		+-- Salvataggio e caricamento
	     * 		|
	     * 		+-- Storico delle partite
	     */
	    
        // Creazione del nodo radice 
        Nodo radice = new Nodo("Guida al Sette e Mezzo", HTML_PATH + "Help.html");
        
        
	    /********* Aggiunta del nodo intermedio "Il gioco del Sette e Mezzo" *********/
	    nodoInt = new DefaultMutableTreeNode("Il gioco del Sette e Mezzo");
	    radice.add(nodoInt);
	    	
		    // Aggiunta del nodo foglia "Basi del gioco del Sette e Mezzo"
	        foglia = new Nodo("Basi del gioco del Sette e Mezzo", 
	        					HTML_PATH + "Basi Sette e Mezzo.html");
	        nodoInt.add(foglia);
	        
	    	// Aggiunta del nodo foglia "Cenni preliminari"
	    	foglia = new Nodo("Cenni preliminari", 
	    						HTML_PATH + "Cenni preliminari.html");
	        nodoInt.add(foglia);
	        
	        // Aggiunta del nodo foglia "Basi del gioco del Sette e Mezzo"
	        foglia = new Nodo("Svolgimento del gioco", 
	        					HTML_PATH + "Svolgimento del gioco.html");
	        nodoInt.add(foglia);
	        
	    /********* Aggiunta del nodo intermedio "Iniziamo a giocare" *****************/
	    nodoInt = new DefaultMutableTreeNode("Iniziamo a giocare");
	    radice.add(nodoInt);
	    	
			// Aggiunta del nodo foglia "Avviare il gioco"
	    	foglia = new Nodo("Avviare il gioco", 
	    						HTML_PATH + "Avviare SetteEMezzo.html");
	        nodoInt.add(foglia);
	
	        // Aggiunta del nodo foglia "Barra dei menu'"
	        foglia = new Nodo("Barra dei menu'", 
	        					HTML_PATH + "Barra dei menu.html");
	        nodoInt.add(foglia);
	        
	        // Aggiunta del nodo foglia "Area di gioco"
	    	foglia = new Nodo("Area di gioco", 
	    						HTML_PATH + "Area di gioco.html");
	    	nodoInt.add(foglia);
	        
	    /********* Aggiunta del nodo intermedio "Opzioni per i registrati" ***********/
        nodoInt = new DefaultMutableTreeNode("Opzioni per i registrati");
	    radice.add(nodoInt);
	    	
			// Aggiunta del nodo foglia "Come registrarsi"
	    	foglia = new Nodo("Come registrarsi", 
	    						HTML_PATH + "Fase di registrazione.html");
	    	nodoInt.add(foglia);
	    	
	    	// Aggiunta del nodo foglia "Come autenticarsi"
	    	foglia = new Nodo("Come autenticarsi", 
	    						HTML_PATH + "Fase di autenticazione.html");
	    	nodoInt.add(foglia);
	    
	        // Aggiunta del nodo foglia "Salvataggio e caricamento"
	        foglia = new Nodo("Salvataggio e caricamento", 
	        					HTML_PATH + "Salva e carica.html");
	        nodoInt.add(foglia);
	        
	        // Aggiunta del nodo foglia "Storico delle partite"
	    	foglia = new Nodo("Storico delle partite", 
	    						HTML_PATH + "Storico delle partite.html");
	    	nodoInt.add(foglia);
	    	
	    return radice;
	}

	// Classe che costruisce un pannello con un' immagine di sfondo
	private class PanelImage extends JPanel {
	
		private static final long serialVersionUID = 3559310321717301074L;
		public ImageIcon sfondoPanel;
	
		/**
		 * Costruisce un pannello con un' immagine di sfondo e un layout
		 * @param image L'immagine di sfondo
		 * @param layout Il layout da associare al pannello
		 */
		public PanelImage(String image, LayoutManager layout){
			sfondoPanel = loadImage(image);
			setLayout(layout);
			
			/*
			 *  Settando il pannello come non opaco, esso diviene trasparente,
			 *  prendendo dunque il background dell'immagine sottostante
			 */
			setOpaque(false);
		}
	
		@Override
		public void paint(Graphics g) {
			sfondoPanel.paintIcon(this, g, 0, 0);
			super.paint(g);
		}
	
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(sfondoPanel.getIconWidth(),
					sfondoPanel.getIconHeight());
		}
	}

	/*
	 * Classe che costruisce il pannello ove poggiare le carte del giocatore.
	 * Se il giocatore e' l'avversario i campi relativi al ruolo, puntata, punteggio
	 * e credito saranno di colore arancio scuro, altrimenti saranno di colore bianco
	 */
	private class PanelGioc extends JPanel {
	
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
		public PanelGioc(String titleBorder, String tipoGioc) {
	
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

			// Pannello contenente i campi del giocatore
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
			
			/***** Sistemazione del Pannello per il tavolo del giocatore in esame *****/	      
			
			/*
			 * GridBagConstraints e' una classe utilizzata per specificare i vincoli che
			 * l'elemento aggiunto al contenitore, con GridBagLayout, deve rispettare 
			 */
			GridBagConstraints rigaLayer = new GridBagConstraints();
			
			// riga
			rigaLayer.gridx = 0;
			
			// colonna
			rigaLayer.gridy = yLayer;
			
			// numero di righe che deve occupare
			rigaLayer.gridwidth = 1;
			
			/*
			 * weightx e weighty definiscono i pesi ponderati delle righe e delle
			 * colonne. Infatti per fissare le ampiezze di una colonna basta fissare 
			 * l'ampiezza di una cella della colonna stessa e per fissare l'altezza 
			 * di una riga basta fissare l'altezza di una cella della riga stessa, 
			 * le altre celle si adatteranno espandendosi all'area disponibile restante.
			 * N.B.
			 * Per pesi ponderati si intende che se abbiamo una griglia 2 x 2 e vogliamo 
			 * che l'ampiezza della prima cella sia il 30% dell'ampiezza della seconda, 
			 * devo impostare weightx della cella (0,0) a 30 
			 * e weightx della cella (1,0 [la cella sottostante]) a 100
			 */
			
			// definisce le proporzioni delle colonne
			rigaLayer.weightx = 1.0;
			
			// definisce le proporzioni delle righe
			rigaLayer.weighty = 1.0;
			
			/*
			 * l'elemento deve variare proporzionalmente 
			 * sia in orizzontale che verticale
			 */
			rigaLayer.fill = GridBagConstraints.BOTH;
			add(tavolo, rigaLayer);
	
			/****************** Sistemazione pannello dei campi utente****************/	   	
	
			GridBagConstraints rigaCampi = new GridBagConstraints();
			rigaCampi.gridx = 0;
			rigaCampi.gridy = yCampi;
			rigaCampi.weighty = weightyCampi;
			rigaCampi.fill = GridBagConstraints.BOTH;
			add(campi, rigaCampi);
	
			// Crea un bordo con effetto "inciso" e un titolo (come fieldset in html)
			setBorder(
					new TitledBorder(
							new EtchedBorder(colore, colore), titleBorder,
							TitledBorder.DEFAULT_JUSTIFICATION,
							TitledBorder.DEFAULT_POSITION, null, null));
		}
	}
}