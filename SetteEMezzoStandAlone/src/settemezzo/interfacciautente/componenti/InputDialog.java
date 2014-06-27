package settemezzo.interfacciautente.componenti;

import java.awt.*;
import java.beans.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settemezzo.interfacciautente.util.iCondition;

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
public class InputDialog extends JDialog{
	
	private static final long serialVersionUID = -7096167618557661361L;
	
	// Condizione da verificare
	private iCondition condizione;
	private JOptionPane optionPane;
	private String typedText1 = null;
    private JLabel[] label;

    /**
     * Costruisce una finestra modale di input 
     * ed effettua delle verifiche sul valore di input
	 * @param contenitore Il contenitore in base al quale posizionare il JDialog
     * @param condtn La condizione da verificare
     * @param title Il titolo della finestra
     * @param msg Il messaggio di richiesta input
     */
	public InputDialog(Container contenitore, iCondition condtn, String title, String msg){
		/*
		 * Costruzione di un JDialog modale, che blocca il frame passato come argomento
		 */
		setTitle(title);
		setModal(true);
		
		condizione = condtn;
		
		label = new JLabel[2];
		
		label[0] = new JLabel(msg, SwingConstants.CENTER);
		label[1] = new JLabel("<html><br /></html>",SwingConstants.CENTER);
		
		/*
		 * Creazione del JOptionPane passando gli elementi da visualizzare,
		 * il tipo di finestra e i pulsanti previsti
		 */
		optionPane = new JOptionPane(label, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		
		/*
		 * Con il metodo setWantsInput(true) si obbliga l'utente a fornire
		 * necessariamente un input
		 */
		optionPane.setWantsInput(true);
				
		/*
		 * Sfruttando il metodo addPropertyChangeListener
		 * colleghiamo l'interfaccia PropertyChangeListener a JOptionPane,
		 * controllando cose' l'inserimento dell'input dell'utente.
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
	public InputDialog(Container contenitore, final JSlider slider, String title, String msg, 
					final String unitaMinura, final NumberFormat numberFormatter) {
		setModal(true);
    	setTitle(title);
    	setMinimumSize(new Dimension(500,230));
    	
		JPanel content = new JPanel(new GridLayout(3,1));
		
		label = new JLabel[2];
		
		label[0] = new JLabel(msg, SwingConstants.CENTER);
		content.add(label[0]);
		
		label[1] = new JLabel(slider.getMinimum() + unitaMinura, SwingConstants.CENTER);
		content.add(label[1]);
		
		slider.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent evento) {
				Double amount = new Double(slider.getValue());
				NumberFormat formattazione = numberFormatter;
				String amountOut = formattazione.format(amount);
				label[1].setText(String.valueOf(amountOut) + unitaMinura);
			}
		});
		content.add(slider);
		
		/*
		 * Creazione del JOptionPane passando gli elementi da visualizzare,
		 * il tipo di finestra e i pulsanti previsti
		 */
		optionPane = new JOptionPane(content, JOptionPane.QUESTION_MESSAGE, 
													JOptionPane.DEFAULT_OPTION);
		
		optionPane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, 
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// Recupero input inserito dall'utente togliendo eventuali punti
				String tmp = label[1].getText();
				
				/*
				 * Imposto il valore di tupedText a quello scelto dall'utente
				 * rimuovendo i caratteri relativi all'unit� di misura
				 */
				typedText1 = tmp.substring(0, tmp.length()- unitaMinura.length());
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
	 * Restituisce la stringa che l'utente ha digitato
	 * @return la stringa che l'utente ha digitato
	 */
	public String getValidatedText(){
		return typedText1;
	}
	
	/*
	 * Classe che gestisce l'evento "cambiamento valore in un JOptionPane"
	 */
	private class ChangeText implements PropertyChangeListener{
	
		@Override
		public void propertyChange(PropertyChangeEvent evento){
	
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
				 * queste istruzioni non verranno eseguite, poiche'� 
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
}