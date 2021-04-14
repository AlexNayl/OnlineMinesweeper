package client;

import client.network.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class Controller {
	private static Controller ownInstance;

	@FXML
	Label ipLabel;

	@FXML
	Label portLabel;

	@FXML
	Label userLabel;

	@FXML
	TextField ipField;

	@FXML
	TextField portField;

	@FXML
	TextField userNameField;

	@FXML
	Button login;

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
	private int port = 16823;

	ConnectionManager connectionManager;
	Thread connectionManagerThread;

	private int demention;
	MineSweeperLogic board;
	Boolean [][] isPressed;
	int numBombs;
	int [][] bombCoor;
	int bombsGotten = 0;
	int whiteSpacePressed = 0;
	String userName = "Jess";

	public void initialize(){
		ownInstance = this;	//Singleton instance
	}

	public static Controller getInstance(){
		return ownInstance;
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
			case "SEND":
				System.out.println("Score Sent");
				break;
			default:
				System.out.println( "Invalid command " + command );
		}
	}

	public void handleSendTestCommand(){
		connectionManager.send("TEST", "sampleString1 \n sampleString2");
		System.out.println("Sent test command.");
	}


	/**
	 * this button sets the demension of the minesweeper board
	 * as well as the number of mines within the board
	 *
	 * @param action
	 */
	public void easy(ActionEvent action) {
		demention = 10;
		numBombs = 9;
		createBoard();
	}

	/**
	 * this button sets the demension of the minesweeper board
	 * as well as the number of mines within the board
	 *
	 * @param action
	 */
	public void medium(ActionEvent action) {
		demention = 16;
		numBombs = 40;
		createBoard();
	}

	/**
	 * this button sets the demension of the minesweeper board
	 * as well as the number of mines within the board
	 *
	 * @param action
	 */
	public void hard(ActionEvent action) {
		demention = 25;
		numBombs = 99;
		createBoard();

	}

	/**
	 * Create board creates an instance of the Minesweeper board with the proper demensions
	 * it will then get the location of the mines, as well as set up the boolean 2D array is Pressed
	 * which records what button on the UI the client has clicked
	 *
	 */
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

	/**
	 * createButtonChart creats and shows all the buttons that will be apart of the UI for the user to click
	 */
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

	/**
	 * this button event determines what button was clicked and associated that with the minesweeper board and
	 * the ispressed array. It will check what value in the minesweeper board corrisponds and change the button to
	 * an uneditable textfeild that shows the user what the value is
	 *
	 * @param event
	 */
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

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
					"Oof, you hit a mine. Game Over.",
					ButtonType.CLOSE);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.CLOSE){
				gameOver();
			}
		} else if (checkNum == 0) {
			whiteSpacePressed++;
			clearWhiteSpace(x,y);
		} else if (checkNum > 0) {
			field.setText(num);
			gridpane.add(field, x, y);
			checkGetBomb();
		}

	}

	/**
	 * clearWhiteSpace is a recusive method that clears all connented buttons that have the value 0
	 * @param x
	 * @param y
	 */
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

	/**
	 * checkGetBomb looks at each bomb coordinate and checks to see if every button around it has been pressed
	 * If so the bomb has been found and the client is close to winning
	 */
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
					bombsGotten++;
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

	/**
	 * checks to see if all the bombs have been found, if so the client wins and game resets
	 */
	private void checkWin() {

		if (bombs.getText().equals("0")) {

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
					"Congratulations, you win!",
					ButtonType.CLOSE);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.CLOSE){
				gameOver();
			}
		}
	}

	/**
	 * gameOver clears all the board information and return to main menu
	 */
	private void gameOver() {
		calculateScore();

		gridpane.getChildren().clear();

		for (int i = 0; i < demention +2; i++) {
			for (int j = 0; j < demention + 2; j++) {
				isPressed[i][j] = false;
			}
		}

		demention = 0;
		board = null;

		bombLabel.setVisible(false);
		bombs.setVisible(false);

		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);
	}

	private void calculateScore() {
		int score;
		score = 10 * bombsGotten;
		score = score - (5*whiteSpacePressed);

		if (score < 0)
			score = 0;

		String parameter = userName + ":" + score;

		connectionManager.send("SEND", parameter);
	}

	public void login(ActionEvent event) {
		ip = ipField.getText();
		String portString = portField.getText();
		port = Integer.parseInt(portString);
		userName = userNameField.getText();

		connectionManager = ConnectionManager.getInstance();
		connectionManager.setConnection( ip, port );
		connectionManagerThread = new Thread(connectionManager);
		connectionManagerThread.start();
		connectionManager.setThread( connectionManagerThread );

		ipLabel.setVisible(false);
		portLabel.setVisible(false);
		userLabel.setVisible(false);
		ipField.setVisible(false);
		portField.setVisible(false);
		userNameField.setVisible(false);
		login.setVisible(false);

		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);

	}
}
