package client;

import client.network.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static ConnectionManager connectionManager;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 850, 850));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        System.exit(0);        //Forces all threads to stop when the window is closed
    }

    /**
     * sets the connection manager in charge of the program
     * Used to move connections between scenes
     * @param newConnectionManager
     */
    public static void setConnectionManager(ConnectionManager newConnectionManager){
        connectionManager = newConnectionManager;
    }

    /**
     * gets the connection manager in charge of the program
     * used to move connections between scenes
     * @return connectionManager
     */
    public static ConnectionManager getConnectionManager(){
        return connectionManager;
    }
}
