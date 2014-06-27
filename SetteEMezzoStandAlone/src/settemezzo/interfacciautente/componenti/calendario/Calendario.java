package settemezzo.interfacciautente.componenti.calendario;

import java.awt.*;
import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Giorno </p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Costruisce un calendario grafico
 * attraverso le swing </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */

public class Calendario extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 8913369762644440133L;

	/*
	 * L'oggetto Calendar permette di convertire un oggetto date 
	 * in elementi di tipo intero indicanti anni, mesi, giorni, ore ecc
	 */
	private static Calendar calendario;

	// Il campo Giorno del Calendario
	private Giorno sceltaGiorno;

	// Il campo Mese del Calendario 
	private Mese sceltaMese;

	// Il campo Anno del Calendario 
	private Anno sceltaAnno;

	// Il pannello che conterra' i campi Mese e Anno
	private JPanel meseAnno;

	// Il giorno che l'utente ha cliccato
	private static String giornoScelto;
	
	// Proprieta' che viene modificata in seguito alla scelta del giorno
	private String property;
	
	/**
	 * Crea un pannello contenente un calendario all'interno di un frame
	 * @param numClic Il numero di click necessari per la selezione del giorno
	 * @param property In nome della proprieta' modificata in seguito alla
	 * scelta del giorno 
	 */
	public Calendario(int numClic, String property) {

		// Selezione del layout del pannello contenitore
		setLayout(new BorderLayout());
		/*
		 * Calendar e' una classe astratta, pertanto, per ottenere
		 * un oggetto del suo tipo, e' necessario utilizzare il metodo getInstance.
		 * Quest'ultimo restituisce un oggetto i cui campi sono stati inizializzati 
		 * con la data e l'ora corrente
		 */
		calendario = Calendar.getInstance();
		
		this.property = property;

		// Creazione di un pannello che contenga il campo per la scelta del Giorno
		sceltaGiorno = new Giorno(numClic);
		sceltaGiorno.addPropertyChangeListener(this);

		// Aggiounta di tale campo al centro del pannello contenitore
		add(sceltaGiorno, BorderLayout.CENTER);

		/*
		 * Creazione di un pannello che contenga 
		 * i campi per la scelta del Mese e dell'Anno
		 */
		meseAnno = new JPanel(new GridLayout(0,2));
		meseAnno.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		// Aggiounta di tale campo al centro del pannello contenitore
		add(meseAnno, BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.SOUTH);
		add(new JPanel(), BorderLayout.EAST);
		add(new JPanel(), BorderLayout.WEST);

		/*
		 *  Creazione di un pannello che contenga il campo per la scelta del Mese
		 *  N.B. E' necessario inizializzare prima l'anno perche'
		 *  altrimenti ogni 12 mesi modificati nello spinner mese
		 *  non cambierebbe automaticamente l'anno
		 */
		
		sceltaAnno = new Anno();
		sceltaAnno.setCampoGiorno(sceltaGiorno);
		sceltaAnno.addPropertyChangeListener(this);

		// Creazione di un pannello che contenga il campo per la scelta del Mese
		sceltaMese = new Mese();
		sceltaMese.setCampoAnno(sceltaAnno);
		sceltaMese.setCampoGiorno(sceltaGiorno);
		sceltaMese.addPropertyChangeListener(this);

		meseAnno.add(sceltaMese);
		meseAnno.add(sceltaAnno);

		setCalendar(calendario, true);
	}

	/*
	 * Aggiorna il calendario quando viene modificato un campo giorno, anno, mese
	 * o se viene modificata un'intera data,
	 * o se l'utente clicca n volte su un determinato giorno  
	 * @param evento L'evento scatenato
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evento) {
		// Se è stato inizializzato un oggetto Calendar
		if (calendario != null) 
		{
			// Crea una copia dell'oggetto
			Calendar c = (Calendar) calendario.clone();

			// Se l'utente ha modificato il campo giorno
			if (evento.getPropertyName().equals("giorno")) 
			{
				// Modifica il campo giorno dell'oggetto clonato
				c.set(Calendar.DAY_OF_MONTH, ((Integer) evento.getNewValue()).intValue());
				
				// Aggiorna il calendario grafico, senza aggiornare la data per intero
				setCalendar(c, false);
			}
			// Se l'utente ha modificato il campo mese
			else if (evento.getPropertyName().equals("mese")) 
			{
				// Modifica il campo giorno dell'oggetto clonato
				c.set(Calendar.MONTH, ((Integer) evento.getNewValue()).intValue());
				// Aggiorna il calendario grafico, senza aggiornare la data per intero
				setCalendar(c, false);
			}
			// Se l'utente ha modificato il campo anno
			else if (evento.getPropertyName().equals("anno")) 
			{
				// Modifica il campo giorno dell'oggetto clonato
				c.set(Calendar.YEAR, ((Integer) evento.getNewValue()).intValue());
				// Aggiorna il calendario grafico, senza aggiornare la data per intero
				setCalendar(c, false);
			}
			// Se l'utente ha modificato l'intera data
			else if (evento.getPropertyName().equals("data")) 
			{
				// Modifica il campo giorno dell'oggetto clonato
				c.setTime((Date) evento.getNewValue());
				// Aggiorna il calendario grafico aggiornando la data per intero
				setCalendar(c, true);
			}
			// Se l'utente ha richiesto un evento per una determinata data
			else if (evento.getPropertyName().equals("scelta")) 
			{
				// Imposta il giorno scelto
				giornoScelto = new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date(calendario.getTimeInMillis()));
				
				// Notifica al metodo chiamante che l'utente ha richiesto un'azione
				firePropertyChange(property, false, true);
			}
		}
	}

	

	/**
	 * Imposta gli attributi dell'oggetto calendario
	 * @param nuovoCalendario Il nuovo valore del calendario
	 * @param aggiorna Se aggiornare l'intera data del calendario o meno
	 * @throws NullPointerException se nuovoCalendario e' uguale null
	 */
	private void setCalendar(Calendar nuovoCalendario, boolean aggiorna) 
										throws NullPointerException {
		if (nuovoCalendario == null)
			setGiorno(null);
		
		Calendar oldCalendar = calendario;
		calendario = nuovoCalendario;

		if (aggiorna) 
		{
			sceltaAnno.setAnno(nuovoCalendario.get(Calendar.YEAR));
			sceltaMese.setMese(nuovoCalendario.get(Calendar.MONTH));
			sceltaGiorno.setGiorno(nuovoCalendario.get(Calendar.DATE));
		}

		firePropertyChange("calendar", oldCalendar, calendario);
	}

	/**
	 * Restituisce il giorno per il quale l'utente richiede l'azione
	 * @return il giorno per il quale l'utente richiede l'azione
	 */
	public String daiGiorno() {
		String giorno = giornoScelto;
		giornoScelto = null;
		return giorno;
	}

	/**
	 * Imposta la nuova data da impostare al calendario
	 * @param nuovaData La nuova data da impostare
	 * @throws NullPointerException se la data e' nulla
	 */
	public void setGiorno(Date nuovaData) throws NullPointerException {
		Date oldDate = calendario.getTime();
		calendario.setTime(nuovaData);
		int year = calendario.get(Calendar.YEAR);
		int month = calendario.get(Calendar.MONTH);
		int day = calendario.get(Calendar.DAY_OF_MONTH);

		sceltaAnno.setAnno(year);
		sceltaMese.setMese(month);
		sceltaGiorno.setCalendar(calendario);
		sceltaGiorno.setGiorno(day);

		firePropertyChange("data", oldDate, nuovaData);
	}
}