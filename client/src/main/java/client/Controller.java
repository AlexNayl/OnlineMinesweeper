package client;

import client.network.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Controller {



	//TODO: remove hardcoding
	private String ip = "127.0.0.1";
	private int port = 555;

	ConnectionManager connectionManager;
	Thread connectionManagerThread;
	private Button button;

	public void handleConnect(){
		connectionManager = ConnectionManager.getInstance();
		connectionManager.setConnection( ip, port );
		connectionManagerThread = new Thread(connectionManager);
		connectionManagerThread.start();
		connectionManager.setThread( connectionManagerThread );
	}

	public void handleSendTestCommand(){
		connectionManager.send("TEST", "sampleString1 \n sampleString2");
		System.out.println("Sent test command.");
	}




}
