package client.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionManager implements Runnable{

	//Used to verify we're talking to an actual game server
	private final String CHALLENGE = "!aiF!hFs3c785cS6";
	private final String REPLY = "ky&skcHgSB@65x5q";

	private String ip = null;
	int port;

	Socket socket;

	Scanner inputStream;
	PrintWriter outputStream;

	//Stored in its own object for easy retrieval, doable since we expect only one thread;
	Thread selfThread;

	/**
	 * Constructs a new connection manager
	 * @param ip Address of server your connecting too
	 * @param port Port of server your connecting too
	 */
	public ConnectionManager(String ip, int port){
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

		System.out.println("Client-Server connection successfully established");
		//TODO: Listen for commands

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
		return true;
	}

	/**
	 * shuts down the socket and lets the thread stop
	 */
	public void terminate(){
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

}
