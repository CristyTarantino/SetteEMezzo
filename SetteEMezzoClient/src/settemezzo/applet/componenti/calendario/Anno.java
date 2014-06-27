package settemezzo.applet.componenti.calendario;

import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Anno </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Crea un JSpinner 
 * nella quale e' possibile selezionare l'anno</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

final class Anno extends JPanel implements ChangeListener{
	private static final long serialVersionUID = 2648810220491090064L;
	
	private Giorno campoGiorno;
    private JSpinner spinner;
    private int vecchioAnno;
    
    /**
     * Costruisce un JSpinner non editabile
     * con valore iniziale, l'anno corrente e con valori
     * minimo e massimo, quelli previsti da <code>Calendar</code>
     */
    public Anno(){
        Calendar calendar = Calendar.getInstance();
        campoGiorno = null;
        
        SpinnerNumberModel model = 
        	new SpinnerNumberModel(calendar.get(Calendar.YEAR),
        								calendar.getMinimum(Calendar.YEAR),
        									calendar.getMaximum(Calendar.YEAR),
        											1);
        
        
		// Creazione di uno spinner e relativa assegnazione dell'item Listener
		spinner = new JSpinner();
		spinner.setModel(model);
		
		/*
		 * Le cifre pari a zero non vengono considerate, dunque, per l'anno
		 * non apparira' con la virgola come nei normali JSpinner 
		 */
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
		editor.getTextField().setEditable(false);
		editor.setBorder(new EmptyBorder(0, 0, 2, 0));
		
				
		spinner.setEditor(editor);
		spinner.setPreferredSize(spinner.getPreferredSize());
		spinner.addChangeListener(this);
		
		// Aggiunta di un bordo vuoto allo spinner
		spinner.setBorder(new EmptyBorder(0, 0, -2, 0));

		// Sistemazione dello spinner nella parte destra del pannello
		add(spinner);
		
    }

    /**
     * Sostituisce l'anno precedentemente impostato
     * con quello scelto dall'utente
     * @param anno Il nuovo anno scelto dall'utente
     * @see #daiAnno
     */
    public void setAnno(int anno) {
    	
        spinner.setValue(anno);

        /*
         * Se il campo contenente il giorno
         * e' diverso da null imposta il valore dell'anno
         * con quello appena impostato
         */
        if (campoGiorno != null) 
            campoGiorno.setAnno(anno);
        
        spinner.setValue(new Integer(anno));
        firePropertyChange("anno", vecchioAnno, anno);
        vecchioAnno = anno;
    }

    /**
     * Imposta il valore dell'anno
     * @param valore Il valore dell'anno
     */
    public void setValore(int valore) {
        setAnno(valore);
    }

    /**
     * Restituisce il valore dell'anno
     * @return Il valore dell'anno
     */
    public int daiAnno() {
    	SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
		return model.getNumber().intValue();
    }

    /**
     * Imposta il campo il campo giorno in modo tale che
     * possa essere direttamente aggiornato
     * @param campoGiorno Il campo giorno del calendario
     */
    public void setCampoGiorno(Giorno campoGiorno) {
        this.campoGiorno = campoGiorno;
    }

    // Quando l'utente modifica il campo anno
	@Override
	public void stateChanged(ChangeEvent arg0) {
		SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
		int anno = model.getNumber().intValue();
		setValore(anno);
	}
}