package server.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Contains information regarding the client, and functions for transmitting to them, also acts as a receiving thread
 */
public class ClientInstance implements Runnable{

	//Used to verify we're talking to an actual game client
	private final String CHALLENGE = "!aiF!hFs3c785cS6";
	private final String REPLY = "ky&skcHgSB@65x5q";

	Socket socket = null;
	int clientId = -1;

	Scanner inputStream;
	PrintWriter outputStream;

	/**
	 * Constructs a new ClientInstance bound to the specified socket.
	 * @param socket
	 * @param clientID unique integer given to each client
	 */
	ClientInstance( Socket socket, int clientID){
		this.socket = socket;
		this.clientId = clientID;
	}

	/**
	 * Intended to be ran as a thread only.
	 */
	public void run(){
		System.out.println("Client " + clientId + " connected.");
		if(!connectAndTest()){
			//Connection failed
			terminate();
			return;
		}

		//TODO: listen for commands

		terminate();
	}

	/**
	 * shuts down the socket and lets the thread stop
	 */
	public void terminate(){
		System.out.println("Client " + clientId + " disconnected.");
		//Remove self from client list
		ClientManager.getInstance().removeClient( clientId );
		try {
			socket.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}

	}

	/**
	 * Sets up input streams for new connection, then verifies game client using "Challenge, Response, Acknowledge"
	 * @return connection successful
	 */
	private boolean connectAndTest(){
		if(clientId < 0){
			System.err.println("Client created without valid client id");
		}

		try {
			inputStream = new Scanner( socket.getInputStream() );
			outputStream = new PrintWriter( socket.getOutputStream() );
			socket.setSoTimeout( 500 );
			String challenge = "";
			if(inputStream.hasNext()) {
				challenge = inputStream.nextLine();
			}

			if(challenge.equals( CHALLENGE )){
				outputStream.println(REPLY);
				outputStream.flush();
			}else{
				//Invalid challenge
				System.err.println("Invalid challenge or no challenge issued");
				terminate();
				return false;
			}
			//Check for acknowledgment
			if(inputStream.hasNext()){
				inputStream.nextLine(); //drop the acknowledgment, we don't need to check it
			}else{
				System.err.println("Failed to acknowledge");
				terminate();
				return false;
			}
			socket.setSoTimeout( 0 );

		}catch(Exception exception){
			//No response
			exception.printStackTrace();
			terminate();
			return false;
		}

		return true;
	}
}
