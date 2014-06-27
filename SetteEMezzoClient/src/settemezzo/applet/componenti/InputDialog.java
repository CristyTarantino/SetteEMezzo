package settemezzo.applet.componenti;

import java.awt.*;
import java.beans.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settemezzo.applet.util.iCondition;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp InputDialog</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Classe che costruisce 
 * finestre modali di input </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class InputDialog extends JDialog {
	
	private static final long serialVersionUID = -7096167618557661361L;
	
	// Condizione da verificare
	private iCondition condizione;
	private JOptionPane optionPane;
	private String typedText1 = null;
	private String typedText2 = null;
    private JLabel[] label;
    private JTextField textField;
    private JPasswordField passwordField;

    /**
     * Costruisce una finestra modale di input 
     * ed effettua delle verifiche sul valore di input
	 * @param contenitore Il contenitore in base al quale posizionare il JDialog
     * @param condtn La condizione da verificare
     * @param title Il titolo della finestra
     * @param msg Il messaggio di richiesta input
     */
	public InputDialog(Container contenitore, iCondition condtn, String title, String msg){
		// Costruzione di un JDialog modale, che blocca il frame passato come argomento
		setTitle(title);
		setModal(true);
		
		condizione = condtn;
		
		label = new JLabel[2];
		
		// JLabel per visualizzare il messaggio di richiesta
		label[0] = new JLabel(msg, SwingConstants.CENTER);
		
		// JLabel per visualizzare il messaggio di errore
		label[1] = new JLabel("<html><br /></html>", SwingConstants.CENTER);
		
		/*
		 * Creazione del JOptionPane definendo gli elementi da visualizzare,
		 * il tipo di finestra e i pulsanti previsti
		 */
		optionPane = new JOptionPane(label, JOptionPane.QUESTION_MESSAGE, 
												JOptionPane.DEFAULT_OPTION);
		
		/*
		 * Con il metodo setWantsInput(true) si obbliga l'utente a fornire
		 * necessariamente un input; viene resa visibile il JTextField del JOptionPane
		 */
		optionPane.setWantsInput(true);
				
		/*
		 * Sfruttando il metodo addPropertyChangeListener
		 * colleghiamo l'interfaccia PropertyChangeListener a JOptionPane,
		 * controllando cosi' l'inserimento dell'input dell'utente.
		 * Quando questi inserira' l'input e clicchera' un pulsante,
		 * si modifichera' il valore della proprieta' value; cio' provochera' 
		 * l'evento PropertyChange e la conseguente attivazione del gestore degli 
		 * eventi, incaricato dell'elaborazione delle informazioni fornite dall'utente  
		 */
		optionPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new ChangeText());
		
		setContentPane(optionPane);
		pack();
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(contenitore);
		setVisible(true);
	}
	
	/**
	 * Costruisce una finestra modale di input per 
	 * la richiesta di un intero compreso in un range attraverso un JSlider
	 * @param contenitore Il contenitore in base al quale posizionare il JDialog
	 * @param slider Lo slider da visualizzare nella finestra modale
	 * @param title Il titolo della finestra
	 * @param msg Il messaggio di richiesta input
	 * @param unitaMisura L'unita' di minura della scala graduata
	 * @param numberFormatter Formattatore del valore numerico da visualizzare
	 */
	public InputDialog(Container contenitore, final JSlider slider, String title, 
			String msg,	final String unitaMisura, final NumberFormat numberFormatter) {
		
		// Costruzione di un JDialog modale, che blocca il frame passato come argomento
		setModal(true);
    	setTitle(title);
    	setMinimumSize(new Dimension(500,230));
    	
    	// JPanel che conterra' le label e lo slider
		JPanel content = new JPanel(new GridLayout(3,1));
		
		label = new JLabel[2];
		
		// JLabel con il messaggio di richiesta
		label[0] = new JLabel(msg, SwingConstants.CENTER);
		content.add(label[0]);
		
		// JLabel per la visualizzazione del valore selezionato
		label[1] = new JLabel(slider.getMinimum() + unitaMisura, SwingConstants.CENTER);
		content.add(label[1]);
		
		// Evento che gestisce il cambiamento della JLabel che mostra il valore
		slider.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent evento) {
				Double amount = new Double(slider.getValue());
				NumberFormat formattazione = numberFormatter;
				String amountOut = formattazione.format(amount);
				label[1].setText(String.valueOf(amountOut) + unitaMisura);
			}
		});
		content.add(slider);
		
		/*
		 * Creazione del JOptionPane passando gli elementi da visualizzare,
		 * il tipo di finestra e i pulsanti previsti
		 */
		optionPane = new JOptionPane(content, JOptionPane.QUESTION_MESSAGE, 
												JOptionPane.DEFAULT_OPTION);
		
		/*
		 * Sfruttando il metodo addPropertyChangeListener
		 * colleghiamo l'interfaccia PropertyChangeListener a JOptionPane,
		 * controllando cosi' l'inserimento dell'input dell'utente.
		 * Quando questi inserira' l'input e clicchera' un pulsante,
		 * si modifichera' il valore della proprieta' value; cio' provochera' 
		 * l'evento PropertyChange e la conseguente attivazione del gestore degli 
		 * eventi, incaricato dell'elaborazione delle informazioni fornite dall'utente  
		 */
		optionPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, 
						new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// Recupero input inserito dall'utente togliendo eventuali punti
				String tmp = label[1].getText();
				
				/*
				 * Imposto il valore di typedText1 a quello scelto dall'utente
				 * rimuovendo i caratteri relativi all'unita' di misura
				 */
				typedText1 = tmp.substring(0, tmp.length()- unitaMisura.toCharArray().length);
				dispose();
			}
		});

        setContentPane(optionPane);
		pack();
		setLocationRelativeTo(contenitore);
		setResizable(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
    }
	
	/**
	 * Costruisce una finestra modale di input con due input per 
	 * la richiesta dei dati di accesso ad un sistema (username, password)
	 * @param contenitore Il contenitore in base al quale posizionare il JDialog
	 * @param condtn La condizione da verificare
     * @param title Il titolo della finestra
     * @param msg Il messaggio di richiesta input
	 * @param primaLab Testo da associare all'etichetta dello username
	 * @param secondaLab Testo da associare all'etichetta della password
	 */
	public InputDialog(Container contenitore, iCondition condtn, String title, 
								String msg, String primaLab, String secondaLab) {
		
		// Costruzione di un JDialog modale, che blocca il frame passato come argomento
		setTitle(title);
		setModal(true);
		condizione = condtn;
		
		label = new JLabel[4];
		
		/*
		 * Creazione di un pannello che sara' usato per contenere
		 * una JTextField, una JPasswordField e due JLabel 
		 */
		JPanel panel = new JPanel();  
		panel.setLayout(new GridLayout(6,1));  
		
		label[0] = new JLabel(msg, SwingConstants.CENTER);
		label[1] = new JLabel("<html><br /></html>", SwingConstants.CENTER);

		// Creazione della label associata al campo username  
		label[2] = new JLabel(primaLab);

		// Creazione della label associata al campo password 
		label[3] = new JLabel(secondaLab);  

		// Creazione del JTextField usato per inserire il nome utente  
		textField = new JTextField(20);  

		// Creazione del JPasswordField usato per inserire la password  
		passwordField = new JPasswordField(10);  

		// Aggiunta la label che presenta il messaggio di richiesta  
		panel.add(label[0]);  
		
		// Aggiunta la label che presenta l'eventuale messaggio di errore
		panel.add(label[1]);
		
		// Aggiunta la label relativa al campo username
		panel.add(label[2]);

		// Aggiunta del campo username  
		panel.add(textField);  

		// Aggiunta la label relativa al campo password  
		panel.add(label[3]);  

		// Aggiunta del campo password  
		panel.add(passwordField);  
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);  

		/*
		 * Creazione del JOptionPane passando gli elementi da visualizzare,
		 * il tipo di finestra e i pulsanti previsti
		 */
		optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, 
												JOptionPane.OK_CANCEL_OPTION);
		
		/*
		 * Sfruttando il metodo addPropertyChangeListener
		 * colleghiamo l'interfaccia PropertyChangeListener a JOptionPane,
		 * controllando cosi' l'inserimento dell'input dell'utente.
		 * Quando questi inserira' l'input e clicchera' un pulsante,
		 * si modifichera' il valore della proprieta' value; cio' provochera' 
		 * l'evento PropertyChange e la conseguente attivazione del gestore degli 
		 * eventi, incaricato dell'elaborazione delle informazioni fornite dall'utente  
		 */
		optionPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, 
													new ChangeDoubleText());
		
		setContentPane(optionPane);
		pack();
		setResizable(false);
		setLocationRelativeTo(contenitore);
		setVisible(true);
	}  
	
	/**
	 * Costruisce una finestra modale di input 
	 * che mostra, tramite JComboBox, una lista 
	 * @param contenitore Il contenitore in base al quale posizionare il JDialog
     * @param title Il titolo della finestra
     * @param salvataggi Array contenente la lista da visualizzare
     * @param msg Il messaggio di richiesta input
	 */
	public InputDialog(Container contenitore, String title, 
										Object[] salvataggi, String msg) {
		
		typedText1 = (String) JOptionPane.showInputDialog( contenitore, msg, title, 
								JOptionPane.QUESTION_MESSAGE, null, salvataggi, 
													salvataggi[salvataggi.length-1] );
	}

	
	/**
	 * Restituisce la stringa che l'utente ha digitato
	 * @return la stringa che l'utente ha digitato
	 */
	public String getValidatedText() {
		return typedText1;
	}
	
	/**
	 * Restituisce le stringhe che l'utente ha digitato
	 * @return array contenente le stringhe che l'utente ha digitato
	 */
	public String[] getValidatedTexts() {
		// Se i valori da ritornare sono nulli ritorna null...
		if(typedText1 == null || typedText2 == null)
			return null;
		else //  ... ritorna i valori digitati
			return new String[]{typedText1, typedText2};
	}
	
	/*
	 * Classe che gestisce l'evento "cambiamento valore in un JOptionPane"
	 * in relazione ad un solo input
	 */
	private class ChangeText implements PropertyChangeListener {
	
		@Override
		public void propertyChange(PropertyChangeEvent evento) {
	
			// Recupero input inserito dall'utente
			String tempText;
			if(optionPane.getInputValue() == JOptionPane.UNINITIALIZED_VALUE)
				tempText = "";
			else
				tempText = (String)optionPane.getInputValue();
			
			// Recupero, dall'evento, il nome della proprieta' che e' stata modificata 
			String prop = evento.getPropertyName();
			
			/*
			 * Se la finestra e' visibile e 
			 * la proprieta' modificata e' quella relativa ai bottoni oppure al
			 * campo di input
			 */
			if(isVisible() 
						&& (JOptionPane.VALUE_PROPERTY.equals(prop) 
								|| JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)))
			{
				 // Ricava il valore associato al bottone premuto
				Object value = optionPane.getValue();
				
				/*
				 * Se tale valore risulta non inizializzato, 
				 * restituisce il controllo al metodo chiamante (il metodo medesimo)
				 * La prima volta che il metodo viene eseguito, 
				 * dopo la pressione di un pulsante, 
				 * queste istruzioni non verranno eseguite, poiche' 
				 * il valore sara' pari a quello previsto per il pulsante selezionato
				 */
				if(value == JOptionPane.UNINITIALIZED_VALUE)
					return;
			}
			
			/*
			 * Viene resettato il valore del pulsante.
			 * Di conseguenza viene invocato il metodo medesimo.
			 * A seguito di questa nuova invocazione, viene eseguito l'if 
			 * appena sopra, il quale restituira' il controllo all'istruzione 
			 * successiva a questa
			 * 
			 * (Questo meccanismo serve ad evitare la comparsa 
			 * di messaggi d'errore duplicati) 
			 */
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			// Validazione del testo inserito dall'utente
			if (condizione.check(tempText)) 
			{
				typedText1 = tempText;
				dispose();
			}
			else if(isVisible())
			{
				label[0].setText("<html><b>" + label[0].getText() + "</b></html>");
				label[1].setText("<html><b>" + condizione.getKindError() + "</b></html>");
				
				label[0].setForeground(Color.red);
				label[1].setForeground(Color.red);
				
				optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/*
	 * Classe che gestisce l'evento "cambiamento valore in un JOptionPane"
	 * in relazione ad un doppio input
	 */
	private class ChangeDoubleText implements PropertyChangeListener {
	
		@Override
		public void propertyChange(PropertyChangeEvent evento) {
			
			// Se l'utente preme pulsanti diversi da ok chiude la finestra
			if(optionPane.getValue().equals(JOptionPane.CANCEL_OPTION))
				dispose();
			else
			{
				// Recupera input inserito dall'utente
				String primaLab = textField.getText();
				String secondaLab = new String(passwordField.getPassword()); 
				
				
				// Recupera, dall'evento, il nome della proprieta' che e' stata modificata 
				String prop = evento.getPropertyName();
				
				/*
				 * Se la finestra e' visibile e 
				 * la proprieta' modificata e' quella relativa ai bottoni oppure al
				 * campo di input
				 */
				if(isVisible() && (JOptionPane.VALUE_PROPERTY.equals(prop)))
				{
					 // Ricava il valore associato al bottone premuto
					Object value = optionPane.getValue();
					
					/*
					 * Se tale valore risulta non inizializzato, 
					 * restituisce il controllo al metodo chiamante (il metodo medesimo)
					 * La prima volta che il metodo viene eseguito, 
					 * dopo la pressione di un pulsante, 
					 * queste istruzioni non verranno eseguite, poiche'
					 * il valore sara' pari a quello previsto per il pulsante selezionato
					 */
					if(value == JOptionPane.UNINITIALIZED_VALUE)
						return;
				}
				
				/*
				 * Viene resettato il valore del pulsante.
				 * Di conseguenza viene invocato il metodo medesimo.
				 * A seguito di questa nuova invocazione, viene eseguito l'if 
				 * appena sopra, il quale restituira' il controllo all'istruzione 
				 * successiva a questa
				 * 
				 * (Questo meccanismo serve ad evitare la comparsa 
				 * di messaggi d'errore duplicati) 
				 */
				optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
				
				// Validazione del testo inserito dall'utente
				if (condizione.check(primaLab) && condizione.check(secondaLab)) 
				{
					typedText1 = primaLab;
					typedText2 = secondaLab;
					
					dispose();
				}
				// Se il testo non e' corretto mostra i messaggi colorati in rosso segnalando l'errore di digitazione
				else if(isVisible())
				{
					label[0].setText("<html><b>" + label[0].getText() + "</b></html>");
					label[1].setText("<html><b>" + condizione.getKindError() + "</b></html>");
					
					label[0].setForeground(Color.red);
					label[1].setForeground(Color.red);
					
					optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
				}
			}// fine if-else
		
		}// fine metodo

	}// fine Classe privata ChangeDoubleText

}// fine classe InputDialog