package server;

import server.network.ClientManager;

public class Controller {
	private int port = 555;	//TODO: Remove hardcoding


	ClientManager clientManager;
	Thread clientManagerThread;
	public void initialize(){
		clientManager = new ClientManager(port);
		clientManagerThread = new Thread(clientManager);
		clientManagerThread.start();
	}

}
