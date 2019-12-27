package controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;
import objectsClasses.User;
import objectsClasses.Tweet;

import java.io.IOException;
import java.util.ArrayList;

public class CreateAccount1 {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorLabel;

    private PageLoader pageLoader = new PageLoader();

    private Gson gson = new Gson();

    @FXML
    public void onSignUpButtonClicked(ActionEvent event) throws IOException {
        if (passwordField.getText().equals("") || usernameField.getText().equals("")) {
            errorLabel.setText("Enter Password or Username");
        } else {
            String usernameF = usernameField.getText();
            String passwordF = passwordField.getText();
            String userAndPass = usernameF + "/" + passwordF;
            System.out.println(userAndPass);
            Main.clientSocket.informationsToServer.format(Protocol.toProtocol("CreateAccount", "String[].class", userAndPass));
            Main.clientSocket.informationsToServer.format("\n");
            Main.clientSocket.informationsToServer.flush();
            String answerOfServer = Main.clientSocket.informationsFromServer.nextLine();
            if (answerOfServer.equals("false")) {
                errorLabel.setText("Username Already existed");
            } else {
                LogIn.oskol = new User(usernameF,passwordF);
                pageLoader.loading(event, "../style/home_page_1.fxml");
            }
        }
        passwordField.clear();
        usernameField.clear();
    }
}
