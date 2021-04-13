package client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionManager implements Runnable{

	private static ConnectionManager selfInstance;		//Singleton instance

	//Used to verify we're talking to an actual game server
	private final String CHALLENGE = "!aiF!hFs3c785cS6";
	private final String REPLY = "ky&skcHgSB@65x5q";

	private final String END_TOKEN = "<END_DATA>";	//Used to mark the end of a set of data

	private String ip = null;
	private int port;
	private boolean validConnection = false;

	private int clientID;

	Socket socket;

	Scanner inputStream;
	PrintWriter outputStream;

	//Stored in its own object for easy retrieval, doable since we expect only one thread;
	Thread selfThread;

	private ConnectionManager(){
		//Forces singleton
	}

	/**
	 * gets the singleton instance of the connection manager
	 * @return
	 */
	public static ConnectionManager getInstance(){
		if(selfInstance == null){
			selfInstance = new ConnectionManager();
		}
		return selfInstance;
	}

	/**
	 * sets the port and ip to connect to
	 * @param ip Address of server your connecting too
	 * @param port Port of server your connecting too
	 */
	public void setConnection(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

	/**
	 * Intended to be ran as a thread only.
	 */
	public void run(){
		if (!connectAndTest()){
			//Connection failed
			//TODO: inform user connection failed
			terminate();
			return;
		}

		//System.out.println("Client-Server connection successfully established");
		//Listens for commands
		while(inputStream.hasNextLine()){
			String command = inputStream.nextLine();
			String parameter = "";
			String currentLine = inputStream.nextLine();
			//Take new lines until we see the end_token
			while(!currentLine.equals( END_TOKEN )){
				parameter += currentLine + "\n";
				currentLine = inputStream.nextLine();
			}
			parameter = parameter.trim();
			handleReceiveCommand( command, parameter );
		}

		terminate();
	}

	/**
	 * Sets up input streams for new connection, then verifies game server using "Challenge, Response, Acknowledge"
	 * @return connection successful
	 */
	private boolean connectAndTest(){

		try{
			socket = new Socket(ip, port);
		}catch(Exception exception){
			System.err.println("Failed to connect to the server.");
			terminate();
			return false;
		}

		try{
			inputStream = new Scanner(socket.getInputStream());
			outputStream = new PrintWriter( socket.getOutputStream() );
		}catch(Exception exception){
			exception.printStackTrace();
			terminate();
			return false;
		}

		//Verify that we're talking to an actual game server, and not some random open port
		try {
			socket.setSoTimeout( 500 );

			outputStream.println(CHALLENGE);
			outputStream.flush();

			String reply = null;
			if(inputStream.hasNext()) {
				reply = inputStream.nextLine();
			}else{
				//no response
				System.err.println("No response to challenge.");
				terminate();
				return false;
			}
			if(!reply.equals( REPLY )){
				//Wrong reply
				System.err.println("Wrong reply.");
				terminate();
				return false;
			}

			//One more send to acknowledge
			outputStream.println( CHALLENGE );
			outputStream.flush();
			socket.setSoTimeout( 0 );

		}catch(Exception exception){
			//No response
			exception.printStackTrace();
			terminate();
			return false;
		}
		validConnection = true;
		return true;
	}

	/**
	 * Sends the given command and parameter to the server
	 * @param command Unique command identifier, so the server knows how to parse the associated parameter
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
	 * Called when server receives a command from the client, executed on client thread
	 * @param command String identifier for a specific type of message (eg, 'board' might be used to send a game board)
	 * @param parameter Data that's optionally sent along with the command, can be any string excluding '<END_DATA>'
	 */
	private void handleReceiveCommand(String command, String parameter){

		//Implement commands into this switch
		switch(command){
			case "TEST":
				System.out.println("Test print");
				System.out.println(parameter);
				break;
			case "SET_ID":
				clientID = Integer.parseInt( parameter );
				break;
			default:
				System.out.println("Invalid command " + command);
		}

		//TODO: on default send to controller
	}

	/**
	 * shuts down the socket and lets the thread stop
	 */
	public void terminate(){
		validConnection = false;
		try {
			socket.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}

	/**
	 * sets current thread instance
	 * @param thread
	 */
	public void setThread(Thread thread){
		selfThread = thread;
	}

	/**
	 * gets current thread instance
	 * @return thread
	 */
	public Thread getThread(){
		return selfThread;
	}

	/**
	 * gets the client ID:
	 * @return client ID
	 */
	public int getClientID(){
		return clientID;
	}

}
