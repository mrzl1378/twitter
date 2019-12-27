package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageLoader {
    public void loading(ActionEvent event, String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        Parent parent = fxmlLoader.load();
        Scene parentS = new Scene(parent,500,650);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(parentS);
        window.show();
    }
}
