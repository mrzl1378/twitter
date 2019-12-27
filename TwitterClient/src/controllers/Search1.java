package controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import objectsClasses.User;

import java.io.IOException;
import java.util.StringTokenizer;

public class Search1 {

    @FXML
    private VBox resultShow;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button myProfileButton;

    @FXML
    private Button searchUsersButton;

    @FXML
    private ScrollPane resultPane;

    @FXML
    private Button homePageButton;

    private PageLoader pageLoader = new PageLoader();

    private Gson gson = new Gson();

    public static User searchedUserByOskol = null;

    @FXML
    private void onSearchButtonClicked() {

    }

    @FXML
    private void onSearchUsersButtonClicked(ActionEvent event) throws IOException {
        if (searchField.getText().equals(LogIn.oskol.getUsername())) {
            pageLoader.loading(event, "../style/my_profile_1.fxml");
        } else {
            sendOrder("Search","String",searchField.getText());
            String answerOfServer = Main.clientSocket.informationsFromServer.nextLine();
            if (answerOfServer.equals("false")) {
                String user = Main.clientSocket.informationsFromServer.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(user, "&");
                String order = stringTokenizer.nextToken();
                String typeRepresentor = stringTokenizer.nextToken();
                String jsonOfUser = stringTokenizer.nextToken();
                if (order.equals("getUsers") && typeRepresentor.equals("User")) {
                    searchedUserByOskol = gson.fromJson(jsonOfUser, User.class);
                    showUsers(searchedUserByOskol);
                }
            } else if (answerOfServer.equals("true")) {
            }
        }
    }

    @FXML
    private void onMyProfileButtonClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event, "../style/my_profile_1.fxml");
    }

    @FXML
    private void onMenuButtonClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event, "../style/menu_bar_1.fxml");
    }

    @FXML
    private void onHomePageButtonClicked(ActionEvent event) throws IOException {
        pageLoader.loading(event, "../style/home_page_1.fxml");
    }

    private void showUsers(User clash) {
        VBox userShow = new VBox();
        userShow.setAlignment(Pos.CENTER);
        userShow.setSpacing(10);
        HBox topName = new HBox();
        HBox downName = new HBox();
        Region besideName = new Region();
        Region betweenUB = new Region();
        Label nameBar = new Label();
        Label usernameBar = new Label();
        Button profile = new Button("see profile");
        nameBar.setStyle(
                "-fx-background-color: #e1e8ed;" +
                        "-fx-pref-width: 100px; " +
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 450px; " +
                        "-fx-background-radius: 10em; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: arial; "
        );
        usernameBar.setStyle(
                "-fx-background-color: #e1e8ed;" +
                        "-fx-text-fill: #657786;" +
                        "-fx-pref-width: 150px; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 450px; " +
                        "-fx-background-radius: 10em; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: arial; "
        );
        besideName.setStyle(
                "-fx-max-width: 350px;" +
                        "-fx-pref-width: 350px;"
        );
        betweenUB.setStyle(
                "-fx-max-width: 120px;" +
                        "-fx-pref-width: 120px;"
        );
        profile.setStyle(
                "-fx-background-color: #1da1f2;" +
                        "-fx-pref-width: 150px; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-background-radius: 15em; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: arial; " +
                        "-fx-text-fill: #ffffff;"
        );
        nameBar.setText(clash.getName());
        usernameBar.setText("@" + clash.getUsername());
        topName.getChildren().addAll(nameBar, besideName);
        downName.getChildren().addAll(usernameBar, betweenUB, profile);
        userShow.getChildren().addAll(topName, downName);
        userShow.setStyle(
                "-fx-border-width: 0px 0px 1px 0px;" +
                        "-fx-border-color: #e1e8ed;"
        );
        userShow.setPadding(new Insets(35, 25, 25, 25));
        resultShow.getChildren().add(userShow);
        resultShow.setAlignment(Pos.CENTER);
        resultPane.setContent(resultShow);
        profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pageLoader.loading(event,"../style/Profiles_1.fxml");
                } catch (IOException ex) {
                    System.out.println("hi");
                }
            }
        });
    }

    private void sendOrder(String order, String ordertype, String json) {
        Main.clientSocket.informationsToServer.format(Protocol.toProtocol(order,ordertype,json));
        Main.clientSocket.informationsToServer.format("\n");
        Main.clientSocket.informationsToServer.flush();
    }

    private void reloadAndSync() {
        Main.clientSocket.informationsToServer.format(Protocol.toProtocol("UpdateUser",null,null));
        Main.clientSocket.informationsToServer.format("\n");
        Main.clientSocket.informationsToServer.flush();
        String user = Main.clientSocket.informationsFromServer.nextLine();
        LogIn.oskol = gson.fromJson(user,User.class);
    }
}