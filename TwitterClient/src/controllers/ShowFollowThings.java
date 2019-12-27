package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import objectsClasses.User;

import java.io.IOException;

//showing followings
public class ShowFollowThings {

    @FXML
    private Button backButton;

    @FXML
    private ScrollPane resultScroll;

    @FXML
    private VBox resultShow;

    private PageLoader pageLoader = new PageLoader();

    @FXML
    private void initialize(){
        if (PassingInfo.information.equals("My_followings")){
            for (String i : LogIn.oskol.getFollowings()) {
                resultShow.setSpacing(25);
                resultShow.getChildren().add(0 , showUsers(i));
                resultShow.setAlignment(Pos.CENTER);
                resultScroll.setContent(resultShow);
            }
        }else if(PassingInfo.information.equals("My_followers")){
            for (String i :LogIn.oskol.getFollowers()) {
                resultShow.setSpacing(25);
                resultShow.getChildren().add(0 , showUsers(i));
                resultShow.setAlignment(Pos.CENTER);
                resultScroll.setContent(resultShow);
            }
        }else if(PassingInfo.information.equals("Searched_followers")){
            for (String i :Search1.searchedUserByOskol.getFollowers()) {
                resultShow.setSpacing(25);
                resultShow.getChildren().add(0 , showUsers(i));
                resultShow.setAlignment(Pos.CENTER);
                resultScroll.setContent(resultShow);
            }
        }else if(PassingInfo.information.equals("Searched_followings")){
            for (String i :Search1.searchedUserByOskol.getFollowings()) {
                resultShow.setSpacing(25);
                resultShow.getChildren().add(0 , showUsers(i));
                resultShow.setAlignment(Pos.CENTER);
                resultScroll.setContent(resultShow);
            }
        }
    }

    @FXML
    private void onBackButtonClicked(ActionEvent event) throws IOException{
        PassingInfo.setInfo("");
        pageLoader.loading(event,"../style/home_page_1.fxml");
    }

    private VBox showUsers(String clash){
        VBox userShow = new VBox();
        userShow.setAlignment(Pos.CENTER);
        userShow.setSpacing(10);
        HBox topName = new HBox();
        HBox downName = new HBox();
        Region besideName = new Region();
        Region betweenUB = new Region();
        Label nameBar = new Label();
        Label usernameBar = new Label();
        nameBar.setStyle(
                "-fx-background-color: #e1e8ed;"+
                        "-fx-pref-width: 100px; "+
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 450px; " +
                        "-fx-background-radius: 10em; "+
                        "-fx-font-size: 18px; "+
                        "-fx-font-family: arial; "
        );
        usernameBar.setStyle(
                "-fx-background-color: #e1e8ed;"+
                        "-fx-text-fill: #657786;"+
                        "-fx-pref-width: 150px; "+
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 450px; " +
                        "-fx-background-radius: 10em; "+
                        "-fx-font-size: 18px; "+
                        "-fx-font-family: arial; "
        );
        besideName.setStyle(
                "-fx-max-width: 350px;"+
                        "-fx-pref-width: 350px;"
        );
        betweenUB.setStyle(
                "-fx-max-width: 120px;"+
                        "-fx-pref-width: 120px;"
        );
        usernameBar.setText("@"+clash);
        topName.getChildren().addAll(nameBar,besideName);
        downName.getChildren().addAll(usernameBar,betweenUB);
        userShow.getChildren().addAll(topName,downName);
        userShow.setStyle(
                "-fx-border-width: 0px 0px 1px 0px;"+
                        "-fx-border-color: #e1e8ed;"
        );
        userShow.setPadding(new Insets(35,25,25,25));
        return userShow;
    }
}
