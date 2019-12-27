package main;

import com.google.gson.Gson;
import controllers.ClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    public static ClientSocket clientSocket;

    @Override
    public void init() throws Exception{
        clientSocket = ClientSocket.getInstance();
        clientSocket.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Gson gson = new Gson();
        Parent root = FXMLLoader.load(getClass().getResource("../style/sample.fxml"));
        primaryStage.setTitle("Twitter");
        primaryStage.setScene(new Scene(root, 500, 650));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
