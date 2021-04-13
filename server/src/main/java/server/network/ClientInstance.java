package server.network;

import server.Controller;

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

	private final String END_TOKEN = "<END_DATA>";	//Used to mark the end of a set of data

	Socket socket = null;
	int clientId = -1;

	boolean validConnection = false;

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

		//Take and parse commands
		while(inputStream.hasNextLine()){
			String command = inputStream.nextLine();
			String parameter = "";
			String currentLine = inputStream.nextLine();
			//Take new lines until we see the end_token
			while(!currentLine.equals( END_TOKEN )){
				parameter += currentLine + "\n";
				currentLine = inputStream.nextLine();
			}
			handleReceiveCommand( command, parameter );
		}

		terminate();
	}

	/**
	 * Network commands only, game commands go in controller
	 * @param command String identifier for a specific type of message (eg, 'board' might be used to send a game board)
	 * @param parameter Data that's optionally sent along with the command, can be any string excluding '<END_DATA>'
	 */
	private void handleReceiveCommand(String command, String parameter){

		//Implement commands into this switch
		switch(command){

			default:
				Controller.getInstance().handleReceiveCommand( clientId, command, parameter );
		}


	}

	/**
	 * Sends the given command and parameter to the client
	 * @param command Unique command identifier, so the clients knows how to parse the associated parameter
	 * @param parameter Data associated with command, any string (eg, for 'board' command it could be game board data)
	 */
	public void send(String command, String parameter){
		if(!validConnection){
			System.err.println("Attempted to send before a valid connection was made");
		}
		outputStream.println(command);
		outputStream.println(parameter);
		outputStream.println(END_TOKEN);
		outputStream.flush();
	}

	/**
	 * shuts down the socket and lets the thread stop
	 */
	public void terminate(){
		validConnection = false;	//Prevents other things from being sent
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
		validConnection = true;
		send("SET_ID", Integer.toString( clientId ));
		return true;
	}
}
