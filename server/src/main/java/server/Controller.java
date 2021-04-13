package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import server.network.ClientManager;


public class Controller {
	private int port = 555;    //TODO: Remove hardcoding

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

	private static Controller ownInstance;	//Singleton instance

	ClientManager clientManager;
	Thread clientManagerThread;
	private int demention;
	MineSweeperLogic board;
	Boolean [][] isPressed;
	int numBombs;
	int [][] bombCoor;

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

		int[][] bombLocation = board.getBombCoor();

		for (int i = 0; i < numBombs; i++){
			int x = bombLocation[i][0];
			int y = bombLocation[i][1];
			isPressed[x][y] = true;
		}

		for(int i = 0; i < demention +2; i++) {
			for(int j = 0; j < demention +2; j++) {
				System.out.print(isPressed[i][j]);
			}
			System.out.println();
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

		isPressed[y+1][x+1] = true;

		String num = Double.toString(checkNum);
		TextField field = new TextField();
		field.setMaxSize(25,25);
		field.setMinSize(25,25);
		field.setEditable(false);

		if (checkNum == -1) {
			field.setText("*");
			gridpane.add(field, x, y);
			gameOver();
		} else if (checkNum == 0) {
			clearWhiteSpace(x,y);
			checkGetBomb();
		} else if (checkNum > 0) {
			field.setText(num);
			gridpane.add(field, x, y);
			checkGetBomb();
		}



	}

	private void clearWhiteSpace(int x, int y) {
		isPressed[x+1][y+1] = true;
		TextField field = new TextField();
		field.setMaxSize(25,25);
		field.setMinSize(25,25);
		field.setEditable(false);
		double num = board.getNum(x,y);
		if (num > 0)
			field.setText(Double.toString(num));
		else
			field.setText(" ");

		gridpane.add(field, x, y);

		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {
				if (board.getNum(i,j) > -1) {
					if ((i > 0) && (i < 10) && (j > 0) && (j < 10))
						clearWhiteSpace(i,j);
				}
			}
		}

		//return;
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


				System.out.println("");

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
			System.out.println("You winn");
			gameOver();
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

		bombLabel.setVisible(false);
		bombs.setVisible(false);

		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);
	}
}