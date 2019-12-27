package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuBar1 {

    @FXML
    private Button log_out_button;

    @FXML
    private Button backButton;

    private PageLoader pageLoader = new PageLoader();

    @FXML
    private void setLog_out_button(ActionEvent event)throws IOException {
        pageLoader.loading(event,"../style/sample.fxml");
    }

    @FXML
    private void setBackButton(ActionEvent event)throws IOException{
        pageLoader.loading(event,"../style/home_page_1.fxml");
    }

}
