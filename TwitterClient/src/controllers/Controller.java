package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createAccount;

    private PageLoader pageLoader = new PageLoader();

    @FXML
    void onLoginClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event, "../style/Log_in.fxml");
    }

    @FXML
    void onCreateAccountClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event, "../style/create_account_1.fxml");
    }

    @FXML
    void initialize() {
        assert location != null : "fx:id=\"location\" was not injected: check your FXML file 'sample.fxml'.";
    }
}
