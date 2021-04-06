package client;

import client.network.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Controller {

	@FXML
	GridPane gridpane;

	@FXML
	Button easy;

	@FXML
	Button medium;

	@FXML
	Button hard;

	//TODO: remove hardcoding
	private String ip = "127.0.0.1";
	private int port = 555;

	ConnectionManager connectionManager;
	Thread connectionManagerThread;
	private Button button;

	public void handleConnect(){
		connectionManager = new ConnectionManager( ip, port );
		connectionManagerThread = new Thread(connectionManager);
		connectionManagerThread.start();
		connectionManager.setThread( connectionManagerThread );
		Main.setConnectionManager( connectionManager );
	}

	public void easy(ActionEvent action) {
		int demention = 10;
		MineSweeper board = new MineSweeper(demention);
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
	}

	public void medium(ActionEvent action) {
		int demention = 50;
		MineSweeper board = new MineSweeper(demention);
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
	}

	public void hard(ActionEvent action) {
		int demention = 50
		MineSweeper board = new MineSweeper(demention);
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
	}


}
