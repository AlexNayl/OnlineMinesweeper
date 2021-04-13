package server;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import server.network.ClientManager;

import java.awt.event.ActionEvent;

public class Controller {
<<<<<<< HEAD
	private int port = 555;    //TODO: Remove hardcoding

	@FXML
	GridPane gridpane;

	@FXML
	Button easy;

	@FXML
	Button medium;

	@FXML
	Button hard;
=======
	private static Controller ownInstance;	//Singleton instance
	private int port = 555;	//TODO: Remove hardcoding
>>>>>>> 27725b2bec20d9b8b05ce40f8d65f5b1ed0bf07e


	ClientManager clientManager;
	Thread clientManagerThread;
<<<<<<< HEAD
	private int demention;
	MineSweeperLogic board;
	Boolean [][] isPressed;

	public void initialize() {
		clientManager = new ClientManager(port);
		clientManagerThread = new Thread(clientManager);
		clientManagerThread.start();
=======
	public void initialize(){
		ownInstance = this;

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

	public void handleSendTestCommand(){
		clientManager.sendAll("TEST", "sampleString1 \n sampleString2");
		System.out.println("Sent test command.");
>>>>>>> 27725b2bec20d9b8b05ce40f8d65f5b1ed0bf07e
	}


	public void easy(ActionEvent action) {
		demention = 10;
		createBoard(demention);
	}

	public void medium(ActionEvent action) {
		demention = 17;
		createBoard(demention);
	}

	public void hard(ActionEvent action) {
		demention = 25;
		createBoard(demention);

	}

	private void createBoard(int demention) {
		board = new MineSweeperLogic(demention);
		isPressed = new Boolean[demention + 2][demention + 2];
		for(int i = 0; i < demention + 2; i++)
			isPressed[0][i] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[i][0] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[11][i] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[i][11] = true;
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);

		createButtonChart(demention);

	}

	private void createButtonChart(int demention) {
		for (int i = 0; i < demention; i++) {
			for (int j = 0; j < demention; j++) {
				isPressed[i+1][j+1] = false;
				Button button = new Button(" ");
				button.setOnAction(this::checkButton);
				button.setMaxSize(25,25);
				button.setMinSize(25,25);
				gridpane.add(button, i, j);
			}
		}

	}

	private void checkButton(ActionEvent event) {
		Button thisButton = (Button) event.getSource();

		double xPos = thisButton.getLayoutX();
		double yPos = thisButton.getLayoutY();

		int x = (int) xPos / 25;
		int y = (int) yPos / 25;

		double checkNum = board.getNum(y, x);

		String num = Double.toString(checkNum);
		System.out.println(num);
		TextField field = new TextField();
		field.setMaxSize(25,25);
		field.setMinSize(25,25);
		field.setEditable(false);

		if (checkNum == -1) {
			field.setText("*");
			gridpane.add(field, x, y);

			gameOver();
		} else if (checkNum == 0) {
			field.setText(" ");
			gridpane.add(field, x, y);
			isPressed[x+1][y+1] = true;

		} else if (checkNum > 0) {
			field.setText(num);
			gridpane.add(field, x, y);
			isPressed[x+1][y+1] = true;
			checkWin();
		}



	}

	private void checkWin() {
		boolean winner = true;
		boolean bombWinner = true;
		int[][] bombCoor = board.getBombCoor();

		for(int i = 0; i < demention; i++) {
			int x = bombCoor[i][0] + 1;
			int y = bombCoor[i][1] + 1;

			for(int k = x - 1; k < x + 1; k++) {
				for (int j = y - 1; j < y + 1; j++) {
					if (isPressed[k][j] != true) {
						winner = false;
						bombWinner = false;
					}
				}
			}
			if (bombWinner == true) {
				TextField field = new TextField();
				field.setMaxSize(25,25);
				field.setMinSize(25,25);
				field.setEditable(false);
				field.setText("*");
				gridpane.add(field, bombCoor[i][0], bombCoor[i][1]);
				isPressed[x+1][y+1] = true;
			}
		}

		if (winner == true) {
			System.out.println("You winn");
			gameOver();
		} else {
			return;
		}


	}

	private void gameOver() {
		gridpane.getChildren().clear();

		board = null;

		for (int i = 0; i < demention +2; i++) {
			for (int j = 0; j < demention + 2; j++) {
				isPressed[i][j] = false;
			}
		}

		demention = 0;

		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);


	}
}