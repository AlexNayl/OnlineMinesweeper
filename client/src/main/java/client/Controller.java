package client;

import client.network.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {
	private static Controller ownInstance;

	@FXML
	GridPane gridpane;

	@FXML
	Button easy;

	@FXML
	Button medium;

	@FXML
	Button hard;

	@FXML
	Label bombLabel;

	@FXML
	TextField bombs;


	//TODO: remove hardcoding
	private String ip = "127.0.0.1";
	private int port = 555;

	ConnectionManager connectionManager;
	Thread connectionManagerThread;
	private Button button;

	private int demention;
	MineSweeperLogic board;
	Boolean [][] isPressed;
	int numBombs;
	int [][] bombCoor;

	public void initialize(){
		ownInstance = this;	//Singleton instance
	}

	public static Controller getInstance(){
		return ownInstance;
	}

	public void handleConnect(){
		connectionManager = ConnectionManager.getInstance();
		connectionManager.setConnection( ip, port );
		connectionManagerThread = new Thread(connectionManager);
		connectionManagerThread.start();
		connectionManager.setThread( connectionManagerThread );
	}

	/**
	 * Called when server receives a command from the server
	 * @param command String identifier for a specific type of message (eg, 'board' might be used to send a game board)
	 * @param parameter Data that's optionally sent along with the command, can be any string excluding '<END_DATA>'
	 */
	public synchronized void handleReceiveCommand(String command, String parameter) {

		//Implement commands into this switch
		switch ( command ) {
			case "TEST":
				System.out.println("Test print");
				System.out.println(parameter);
				break;
			case "BOARD":

				System.out.println("Recieved board: " + parameter);
				//TODO: update the screen to the new board
				board.field = MineSweeperLogic.toField( parameter, demention );
				clear();

				isPressed = new Boolean[demention + 2][demention + 2];

				for(int i = 0; i < demention + 2; i++)
					isPressed[0][i] = true;
				for(int i = 0; i < demention + 2; i++)
					isPressed[i][0] = true;
				for(int i = 0; i < demention + 2; i++)
					isPressed[11][i] = true;
				for(int i = 0; i < demention + 2; i++)
					isPressed[i][11] = true;

				for (int i = 0; i < numBombs; i++){
					int x = bombCoor[i][0];
					int y = bombCoor[i][1];
					isPressed[x][y] = true;
				}

				for (int i = 1; i < demention +2; i++) {
					for (int j = 1; j < demention +2; j++) {
						if (board.field[i][j] > 10.0){
							isPressed[i][j] = true;
						} else {
							isPressed[i][j] = false;
						}
					}
				}

				for (int i = 1; i < demention +2; i++) {
					for (int j = 1; j < demention +2; j++) {
						if (isPressed[i][j] = true){
							TextField numberClicked = new TextField();
							numberClicked.setMaxSize(25,25);
							numberClicked.setMinSize(25,25);
							numberClicked.setEditable(false);
							double num = board.field[i][j];
							numberClicked.setText(Double.toString(num));
							gridpane.add(numberClicked, i - 1, j - 1);
						} else {
							button.setOnAction(this::checkButton);
							button.setMaxSize(25,25);
							button.setMinSize(25,25);
							gridpane.add(button, i-1, j-1);

						}
					}
				}

				board.printMap();

				break;
			default:
				System.out.println( "Invalid command " + command );
		}
	}

	public void handleSendTestCommand(){
		connectionManager.send("TEST", "sampleString1 \n sampleString2");
		System.out.println("Sent test command.");
	}

	public void easy(ActionEvent action) {
		demention = 10;
		numBombs = 9;
		createBoard();
	}

	public void medium(ActionEvent action) {
		demention = 16;
		numBombs = 40;
		createBoard();
	}

	public void hard(ActionEvent action) {
		demention = 25;
		numBombs = 99;
		createBoard();

	}

	private void createBoard() {
		board = new MineSweeperLogic(demention, numBombs);
		board.printMap();
		bombCoor = board.getBombCoor();
		isPressed = new Boolean[demention + 2][demention + 2];

		for (int i = 0; i < demention +2; i++) {
			for (int j = 0; j < demention +2; j++) {
				isPressed[i][j] = false;
			}
		}

		for(int i = 0; i < demention + 2; i++)
			isPressed[0][i] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[i][0] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[11][i] = true;
		for(int i = 0; i < demention + 2; i++)
			isPressed[i][11] = true;

		for (int i = 0; i < numBombs; i++){
			int x = bombCoor[i][0];
			int y = bombCoor[i][1];
			isPressed[x][y] = true;
		}

		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);

		bombLabel.setVisible(true);
		bombs.setText(Integer.toString(numBombs));
		bombs.setVisible(true);

		createButtonChart();

	}

	private void createButtonChart() {
		for (int i = 0; i < demention; i++) {
			for (int j = 0; j < demention; j++) {
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

		board.field[y][x] += 100;
		isPressed[y+1][x+1] = true;

		String num = Double.toString(checkNum);
		TextField field = new TextField();
		field.setMaxSize(25,25);
		field.setMinSize(25,25);
		field.setEditable(false);

		if (checkNum == -1) {
			field.setText("*");
			gridpane.add(field, x, y);
			//Pop Up
			gameOver();
		} else if (checkNum == 0) {
			clearWhiteSpace(x,y);
		} else if (checkNum > 0) {
			field.setText(num);
			gridpane.add(field, x, y);
			checkGetBomb();
		}

		ConnectionManager.getInstance().send( "BOARD", board.toString() );

	}

	private void clearWhiteSpace(int x, int y) {
		isPressed[x+1][y+1] = true;
		TextField field = new TextField();
		field.setMaxSize(25,25);
		field.setMinSize(25,25);
		field.setEditable(false);
		field.setText(" ");
		gridpane.add(field, x, y);
		double checkNum;

		/*
		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {
				checkNum = board.getNum(i,j);
				//System.out.println(checkNum);
				if ((checkNum == 0.0) && (i > -1) && (i < 11) && (j > -1) && (j < 11) && (isPressed[i + 1][j + 1] == false)) {
				//	clearWhiteSpace(i, j);
				}
			}
		}

		return;

		 */
	}

	private void checkGetBomb() {
		boolean bombWon;

		for (int z = 0; z < numBombs; z++) {
			bombWon = true;

			int xCoor = bombCoor[z][0];
			int yCoor = bombCoor[z][1];

			if ((xCoor > 0) && (yCoor > 0)) {
				for (int i = xCoor - 1; i < xCoor + 2; i++) {
					for (int j = yCoor - 1; j < yCoor + 2; j++) {
						if (isPressed[i][j] != true) {
							bombWon = false;
						}
					}
				}

				if (bombWon == true) {

					TextField field = new TextField();
					field.setMaxSize(25, 25);
					field.setMinSize(25, 25);
					field.setEditable(false);
					field.setText("*");
					gridpane.add(field, yCoor - 1, xCoor - 1);

					String numberOfBombs = bombs.getText();
					int num = Integer.parseInt(numberOfBombs);
					num--;
					bombs.setText(Integer.toString(num));
					checkWin();

					bombCoor[z][0] = -1;
					bombCoor[z][0] = -1;
				}
			}
		}
	}

	private void checkWin() {

		if (bombs.getText().equals("0")) {

			//Pop UP

			gameOver();
		}
	}

	private void gameOver() {

		clear();

		demention = 0;

		bombLabel.setVisible(false);
		bombs.setVisible(false);

		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);
	}

	public void clear() {

		gridpane.getChildren().clear();

		board = null;

		for (int i = 0; i < demention +2; i++) {
			for (int j = 0; j < demention + 2; j++) {
				isPressed[i][j] = false;
			}
		}

	}



}
