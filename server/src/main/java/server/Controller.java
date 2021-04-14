package server;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import server.network.ClientManager;

import java.io.*;


public class Controller {

	@FXML
	TextField first;
	@FXML
	TextField second;
	@FXML
	TextField third;
	@FXML
	TextField fourth;
	@FXML
	TextField fifth;
	@FXML
	TextField sixth;
	@FXML
	TextField seventh;
	@FXML
	TextField eighth;
	@FXML
	TextField ninth;
	@FXML
	TextField tenth;


	private int port = 16823;
	private static Controller ownInstance;	//Singleton instance
	private String[][] pritableScores = new String[10][2];
	private int[] scores = new int[10];
	ClientManager clientManager;
	Thread clientManagerThread;

	public void initialize(){
		ownInstance = this;
		readInSavedScores();

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
			case "SEND":
				parameter = parameter.trim();
				String[] sentScore = parameter.split(":");
				String name = sentScore[0];
				int score = Integer.parseInt(sentScore[1]);
				checkScorePositions(name, score);
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

	/**
	 * This function reads in the file that is connected to the server to get all saved highscores
	 * and puts them into their designated variables
	 */
	private void readInSavedScores() {

		try {
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader("../server/src/main/resources/SavedScores.txt"));
			for (int i = 0; i < 10; i++) {
				line = br.readLine();
				String[] recScore = line.split(":");
				pritableScores[i][0] = recScore[0];
				pritableScores[i][1] = recScore[1];
				scores[i] = Integer.parseInt(recScore[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		printToUI();

	}

	/**
	 * updateFile will re-write the file with the new top scores
	 */
	private void updateFile() {
		try {
			FileWriter myWriter = new FileWriter("../server/src/main/resources/SavedScores.txt");
			for (int i = 0; i < 10; i++){
				myWriter.write(pritableScores[i][0]+":"+pritableScores[i][1] + "\n");
			}
			myWriter.flush();
			myWriter.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * checkScore will take a new username and connected score and check to see if the score is higher than
	 * any of the highscores. If it is it will bump all lower scores down one
	 * @param name is the username
	 * @param newScore is the score being compared
	 */
	private void checkScorePositions(String name, int newScore) {
		int temp;
		String nameTemp;
		for (int i = 0; i < 10; i++) {
			if (scores[i] < newScore) {
				temp = scores[i];
				nameTemp = pritableScores[i][0];
				pritableScores[i][0] = name;
				pritableScores[i][1] = Integer.toString(newScore);
				scores[i] = newScore;
				newScore = temp;
				name = nameTemp;
			}
		}
		printToUI();
		updateFile();
	}

	/**
	 * this function prints the top 10 scores to the UI
	 */
	private void printToUI() {
		first.setText(pritableScores[0][0] + " " + pritableScores[0][1]);
		second.setText(pritableScores[1][0]+ " " + pritableScores[1][1]);
		third.setText(pritableScores[2][0]+ " " + pritableScores[2][1]);
		fourth.setText(pritableScores[3][0]+ " " + pritableScores[3][1]);
		fifth.setText(pritableScores[4][0]+ " " + pritableScores[4][1]);
		sixth.setText(pritableScores[5][0]+ " " + pritableScores[5][1]);
		seventh.setText(pritableScores[6][0]+ " " + pritableScores[6][1]);
		eighth.setText(pritableScores[7][0]+ " " + pritableScores[7][1]);
		ninth.setText(pritableScores[8][0]+ " " + pritableScores[8][1]);
		tenth.setText(pritableScores[9][0]+ " " + pritableScores[9][1]);
	}
}