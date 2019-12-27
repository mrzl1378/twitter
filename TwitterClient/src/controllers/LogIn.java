package controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.Main;
import objectsClasses.User;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class LogIn  {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label errorlabel;

    @FXML
    private PasswordField password_field;

    @FXML
    private Hyperlink sign_in_Button ;


    @FXML
    private Hyperlink forget_password_link;

    @FXML
    private TextField username_field;

    @FXML
    private Button log_in_button;

    private PageLoader pageLoader = new PageLoader();

    Gson gson = new Gson();

    public static User oskol;

    @FXML
    public void onLogInClicked(ActionEvent event) throws Exception{
        String usernameF = username_field.getText();
        String passwordF = password_field.getText();
        String userAndPass = usernameF + "/" + passwordF;
        System.out.println(userAndPass);
        Main.clientSocket.informationsToServer.format(Protocol.toProtocol("Login","String[].class",userAndPass));
        Main.clientSocket.informationsToServer.format("\n");
        Main.clientSocket.informationsToServer.flush();
        String answerOfServer = Main.clientSocket.informationsFromServer.nextLine();
        if (answerOfServer.equals("false")){
            String user = Main.clientSocket.informationsFromServer.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(user,"&");
            String order = stringTokenizer.nextToken();
            String typeRepresentor = stringTokenizer.nextToken();
            String jsonOfUser = stringTokenizer.nextToken();
            if (order.equals("getLoggedUsers") && typeRepresentor.equals("User")){
                oskol = gson.fromJson(jsonOfUser,User.class);
                pageLoader.loading(event,"../style/home_page_1.fxml");
            }
        }else if(answerOfServer.equals("true")){
            errorlabel.setText("Wrong");
        }
    }
    @FXML
    public void onSignInClicked(ActionEvent event) throws IOException{
        pageLoader.loading(event,"../style/create_account_1.fxml");
    }

    @FXML
    public void onPasswordClicked(){
    }
}
