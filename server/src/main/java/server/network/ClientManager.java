package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Acts as a listen server and client manager when ran as a thread
 * Is singleton
 */
public class ClientManager implements Runnable{
	private static ClientManager singletonInstance;

	private final int MAX_CONNECTIONS = 50;

	private int port = -1;
	private boolean running = false;

	private ServerSocket listener;
	ExecutorService threadPool;

	private Map<Integer, ClientInstance> activeClientMap;	//list of clients indexed by their client id
	private int clientIndex = 0; //Gets indexed by one each time a client joins, used as each clients id

	/**
	 * defeats outside instantiation, singleton only
	 */
	private ClientManager(){
		activeClientMap = new TreeMap<>();
	}
	/**
	 * gets the singleton instance
	 */
	public static ClientManager getInstance(){
		if(singletonInstance == null){
			singletonInstance = new ClientManager();
		}
		return singletonInstance;
	}

	/**
	 * sets the port, needed before its runnable
	 * @param port port to listen on
	 */
	public void setPort(int port){
		this.port = port;
	}

	/**
	 * Intended to be ran as a thread only.
	 */
	public void run(){
		running = true;

		//Prep
		try{
			listener = new ServerSocket(port);
			listener.setSoTimeout( 1000 );	//Forces accept() to stop every second to recheck if server still running
			threadPool = Executors.newFixedThreadPool( MAX_CONNECTIONS );
		}catch(Exception exception){
			//Port probably already in use
			System.err.println(exception.getMessage());
			running = false;
		}
		if(port < 0){
			System.err.println("Attempted to start ClientManager without proper port");
			running = false;
		}

		//Main Loop
		while(running){
			try{
				Socket newSocket = listener.accept();
				ClientInstance newClient = new ClientInstance( newSocket , clientIndex);

				threadPool.execute( newClient );
				activeClientMap.put(clientIndex, newClient);
				clientIndex++;
			}catch( SocketTimeoutException exception ){
				//Do nothing, frequent timeout expected as server needs to check if its still running
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}

		//Shutdown
		running = false; //Just in case this didn't happen for some reason
		try {
			listener.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}

	/**
	 * shuts down the socket and lets the thread stop
	 */
	public void terminate(){
		try {
			running = false;
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}

	/**
	 * Removes the client from the list, doesn't kill the client
	 * Called by client on shutdown
	 * @param clientId
	 */
	void removeClient(int clientId){
		activeClientMap.remove( clientId );
	}

	/**
	 * gets the current active client map, keys are client id integers, objects are clientInstances
	 * @return clientMap
	 */
	public Map<Integer, ClientInstance> getActiveClientMap(){
		return activeClientMap;
	}
}
