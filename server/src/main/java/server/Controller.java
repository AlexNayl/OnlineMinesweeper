package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import server.network.ClientManager;


public class Controller {
	private int port = 555;    //TODO: Remove hardcoding



	private static Controller ownInstance;	//Singleton instance

	ClientManager clientManager;
	Thread clientManagerThread;

	public void initialize(){
		ownInstance = this;

		if(clientManager == null) {
			clientManager = ClientManager.getInstance();
			clientManager.setPort( port );
			clientManagerThread = new Thread( clientManager );
			clientManagerThread.start();
		}
	}

	/**
	 * Called when server receives a command from the client, executed on client thread
	 * @param clientID Unique id pertaining to the clients session (can be used to look up the client in the clientMap)
	 * @param command String identifier for a specific type of message (eg, 'board' might be used to send a game board)
	 * @param parameter Data that's optionally sent along with the command, can be any string excluding '<END_DATA>'
	 */
	public synchronized void handleReceiveCommand( int clientID, String command, String parameter){

		//Implement commands into this switch
		switch(command){
			case "TEST":
				System.out.println("Test print from " + clientID);
				System.out.println(parameter);
				break;
			default:
				System.out.println("Invalid command " + command + " from client " + clientID);
		}


	}

	/**
	 * gets the singleton controller instance
	 * @return controller instance
	 */
	public static Controller getInstance(){
		return ownInstance;
	}

	public void handleSendTestCommand(){
		clientManager.sendAll("TEST", "sampleString1 \n sampleString2");
		System.out.println("Sent test command.");
	}



}