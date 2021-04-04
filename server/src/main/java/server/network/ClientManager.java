package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Acts as a listen server and client manager when ran as a thread
 */
public class ClientManager implements Runnable{

	private final int MAX_CONNECTIONS = 50;

	private int port;
	private boolean running = true;

	private ServerSocket listener;
	ExecutorService threadPool;

	/**
	 * Constructs a new ClientManager that listens on the specified port
	 * @param port port to listen too when running
	 */
	public ClientManager(int port){
		this.port = port;
	}

	/**
	 * Intended to be ran as a thread only.
	 */
	public void run(){

		try{
			listener = new ServerSocket(port);
			listener.setSoTimeout( 1000 );	//Forces accept() to stop every second to recheck if server still running
			threadPool = Executors.newFixedThreadPool( MAX_CONNECTIONS );
		}catch(Exception exception){
			//Port probably already in use
			System.err.println(exception.getMessage());
			running = false;
		}

		while(running){
			try{
				Socket newSocket = listener.accept();
				ClientInstance newClient = new ClientInstance( newSocket );
				threadPool.execute( newClient );
			}catch( SocketTimeoutException exception ){
				//Do nothing, frequent timeout expected as server needs to check if its still running
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}

		//If loop exits, intentional shutdown in progress
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
}
