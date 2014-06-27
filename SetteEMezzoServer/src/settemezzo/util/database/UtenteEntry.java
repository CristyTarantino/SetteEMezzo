package settemezzo.util.database;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp UtenteEntry</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Rappresenta un record
 * della tabella setteemezzo.utente</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class UtenteEntry {
	private static final long serialVersionUID = -2535631685851321241L;
	
	private String nickname;
    private String password;
    private String email;
    
    /**
	 * Imposta il nickname del giocatore
	 * @param nickname il nickname del giocatore
	 */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Imposta l'email del giocatore
     * @param email l'indirizzo di posta elettronica del giocatore
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Imposta la password del giocatore
     * @param password la password del giocatore
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il nickname del giocatore
     * @return il nickname del giocatore
     */
    public String getNickname() { 
    	return nickname; 
    }

    /**
     * Restituisce l'email del giocatore
     * @return l'email del giocatore
     */
    public String getEmail() { 
    	return email; 
    }

    /**
     * Restituisce la password del giocatore
     * @return la password del giocatore
     */
    public String getPassword() { 
    	return password; 
    }
}