package settemezzo.server;

import java.io.*;
import java.net.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp MultiServer</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Metodi Avanzati di Programmazione </p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Server multi-threading
 * per permette l'esecuzione contemporanea di partite a Sette e Mezzo tra due giocatori</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class MultiServer {
	
	private static final int PORT = 6502;
	
	/**
	 * Crea un server multi-threading
	 * e gestisce la connessione dei client
	 */
	public MultiServer() {
		System.out.println("MultiServer is running...");
		
		ServerSocket sSocket = null;
		try 
		{
			// Istanziazione della socket lato server
			sSocket = new ServerSocket(PORT);
			
			while(true)
			{
				System.out.println("Waiting for...");
				
				/*
				 * Il server rimane in attesa di una connessione
				 * da parte di un client.
				 * Quando un giocatore si connettera' al server,
				 * verra' ricava la socket lato client
				 * in modo da poter stabilire una connessione "Socket-to-Socket"
				 */
				Socket socket = sSocket.accept();
				
				try
				{
					// Instanzio un nuovo thread che si occupera' del client
					new ServeOneClient(socket);
				}
				catch(IOException e)
				{ 
					System.err.println("Errore nella creazione della socket!");
					socket.close();
				}
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Errore nella creazione della ServerSocket!");
		} 
		finally
		{
			try 
			{
				// Chiusura dela server socket
				sSocket.close();
			} 
			catch(IOException e)
			{
				System.err.println("Impossibile chiudere la serverSocket!");
			}
		}
	}
	
	/**
	 * Consente l'avvio del server multi-threading
	 * @param args parametri di avvio (non previsti)
	 */
	public static void main(String[] args){
		new MultiServer();		
	}
}