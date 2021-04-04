package client;

import client.network.ConnectionManager;

public class Controller {

	//TODO: remove hardcoding
	private String ip = "127.0.0.1";
	private int port = 555;

	ConnectionManager connectionManager;
	Thread connectionManagerThread;

	public void handleConnect(){
		connectionManager = new ConnectionManager( ip, port );
		connectionManagerThread = new Thread(connectionManager);
		connectionManagerThread.start();
		connectionManager.setThread( connectionManagerThread );
		Main.setConnectionManager( connectionManager );
	}
}
