package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import main.Main;
import objectsClasses.User;

import java.io.IOException;

public class Tweetpage {

    public static int id = 0;
    @FXML
    private Button menubarButton;

    @FXML
    private Button tweetButton;

    @FXML
    private Button backButton;

    @FXML
    private TextArea tweetText;

    @FXML
    private Label errorLabel;

    @FXML
    private Button log_out_button;

    @FXML
    private Button TweetButton;

    private PageLoader pageLoader = new PageLoader();

    @FXML
    void menu(ActionEvent event) {

    }

    @FXML
    void setTweetButton(ActionEvent event) {

    }


    @FXML
    void onTweetButtonClicked(ActionEvent event) throws IOException{
        boolean flag ;
        if(tweetText.getText().length() > 140){
            flag = false;
        }else{
            flag = true;
        }
        if (flag) {
            if (tweetText.getText().length() == 0){
                errorLabel.setText("C'mon type something");
            }else {
                Main.clientSocket.informationsToServer.format(Protocol.toProtocol("Tweet", "String",tweetText.getText()));
                Main.clientSocket.informationsToServer.format("\n");
                Main.clientSocket.informationsToServer.flush();
            }
        }else{
            errorLabel.setText("Your tweet is too long");
        }
        pageLoader.loading(event,"../style/home_page_1.fxml");
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event,"../style/home_page_1.fxml");
    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event,"../style/sample.fxml");
    }

}
