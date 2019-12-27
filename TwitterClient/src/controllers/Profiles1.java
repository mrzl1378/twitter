package controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main;
import objectsClasses.Tweet;
import objectsClasses.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Profiles1 {
    @FXML
    private Button setUpProfileButton;

    @FXML
    private Label followersNumbers;

    @FXML
    private Hyperlink followingsLink;

    @FXML
    private Hyperlink followersLink;

    @FXML
    private ScrollPane tweetsScrollpane;

    @FXML
    private Label followingNumbers;

    @FXML
    private Button backButton;

    @FXML
    private Button followButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private VBox tweetLoader;

    @FXML
    private Button menuBarButton;

    @FXML
    private Label nameLabel;

    private PageLoader pageLoader = new PageLoader();

    private Gson gson = new Gson();

    boolean flag = true;

    @FXML
    void onFollowersLinkClicked(ActionEvent event) throws IOException{
        reloadAndSync();
        PassingInfo.setInfo("Searched_followers");
        pageLoader.loading(event,"../style/show_follow_things.fxml");
    }

    @FXML
    void onFollowingsLinkClicked(ActionEvent event) throws IOException {
        reloadAndSync();
        PassingInfo.setInfo("Searched_following");
        pageLoader.loading(event,"../style/show_follow_things.fxml");
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        reloadAndSync();
        pageLoader.loading(event,"../style/home_page_1.fxml");
    }

    @FXML
    void onMenuBarButtonClicked(ActionEvent event) throws IOException{
        reloadAndSync();
        pageLoader.loading(event,"../style/menu_bar_1.fxml");
    }

    @FXML
    void onSetUpProfileButtonClicked(ActionEvent event) throws IOException{
        reloadAndSync();
        pageLoader.loading(event,"../style/menu_bar_1.fxml");
    }

    @FXML
    void onFollowButtonClicked(ActionEvent event){
        if (flag){
            sendOrder("Follow","User",null);
            reloadAndSync();
            reloadAndSyncSearchedUser();
            followersNumbers.setText(String.valueOf(Search1.searchedUserByOskol.getFollowers().size()));
            flag = false;
            followButton.setText("Unfollow");
        }else{
            sendOrder("UnFollow","User",null);
            reloadAndSync();
            reloadAndSyncSearchedUser();
            followersNumbers.setText(String.valueOf(Search1.searchedUserByOskol.getFollowers().size()));
            flag = true;
            followButton.setText("Follow");
        }
    }

    @FXML
    private void initialize(){
        reloadAndSync();
        reloadAndSyncSearchedUser();
        nameLabel.setText(Search1.searchedUserByOskol.getName());
        usernameLabel.setText(Search1.searchedUserByOskol.getUsername());
        followersNumbers.setText(String.valueOf(Search1.searchedUserByOskol.getFollowers().size()));
        followingNumbers.setText(String.valueOf(Search1.searchedUserByOskol.getFollowings().size()));
        reloadAndSync();
        reloadAndSyncSearchedUser();
        for (String i: LogIn.oskol.getFollowings()) {
            if (i.equals(Search1.searchedUserByOskol.getUsername())){
                followButton.setText("Un follow");
                flag = false;
            }
        }
        showTweets();
    }

    private void showTweets(){
        reloadAndSync();
        reloadAndSyncSearchedUser();
        tweetLoader.setSpacing(25);

        String json = gson.toJson(Search1.searchedUserByOskol);
        sendOrder("GetTimeLineTweetsProfiles","User",json);
        StringTokenizer stringTokenizer = new StringTokenizer(Main.clientSocket.informationsFromServer.nextLine(),"&");
        String order = stringTokenizer.nextToken();
        String jsonType = stringTokenizer.nextToken();
        String jsonTweet = stringTokenizer.nextToken();
        Tweet[] timelineTweetsArray = gson.fromJson(jsonTweet,Tweet[].class);

        Tweet temp = new Tweet();
        boolean flag = true;
        for (int i = 0; i < timelineTweetsArray.length - 1; i++) {
            for (int j = 0; j < timelineTweetsArray.length - i - 1; j++) {
                if (timelineTweetsArray[j].getId() > timelineTweetsArray[j+1].getId()){
                    temp = timelineTweetsArray[j+1];
                    timelineTweetsArray[j+1] = timelineTweetsArray[j];
                    timelineTweetsArray[j] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
        //end of sort
        for (Tweet i : timelineTweetsArray) {
            VBox theTweet = new VBox();
            theTweet.setAlignment(Pos.CENTER);
            theTweet.setSpacing(8);
            HBox namesAndShits = new HBox();
            namesAndShits.setAlignment(Pos.TOP_LEFT);
            namesAndShits.setSpacing(5);
            HBox tweetItSelf = new HBox();
            HBox likesAndShits = new HBox();
            likesAndShits.setAlignment(Pos.TOP_RIGHT);
            likesAndShits.setSpacing(5);
            Label tweetText = new Label();
            tweetText.setAlignment(Pos.TOP_LEFT);
            tweetText.setText(i.getText());
            tweetText.setStyle(
                    "-fx-background-color: #ffffff;" +
                            "-fx-min-width: 450px; " +
                            "-fx-min-height: 30px; " +
                            "-fx-max-width: 450px; " +
                            "-fx-background-radius: 10em; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-family: arial; " +
                            "-fx-padding: 10px;"
            );
            tweetText.setPadding(new Insets(10, 0, 0, 0));
            Label usernameLabel = new Label();
            usernameLabel.setStyle(
                    "-fx-text-fill: #657786;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;"
            );
            usernameLabel.setText("@" + i.getUsername());
            Label likedNumber = new Label();
            likedNumber.setText(Integer.toString(i.getLikes()));
            likedNumber.setStyle(
                    "-fx-text-fill: #000000;" +
                            "-fx-font-size: 14px;"
            );
            Button like = new Button();
            final boolean[] likeFlag = {isLiked(like, likedNumber, i)};
            //handeling button like
            like.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (likeFlag[0]) {
                        String userJson = gson.toJson(LogIn.oskol);
                        String tweetJson = gson.toJson(i);
                        String json = userJson + "/" + tweetJson;
                        System.out.println(json);
                        sendOrder("Like", "Tweet", json);
                        i.setLikes();
                        LogIn.oskol.getLikedTweets().add(i);
                        reloadAndSync();
                        likeFlag[0] = isLiked(like, likedNumber, i);
                    } else {
                        String userJson = gson.toJson(LogIn.oskol);
                        String tweetJson = gson.toJson(i);
                        String json = userJson + "/" + tweetJson;
                        System.out.println(json);
                        sendOrder("UnLike", "Tweet", json);
                        i.deLikes();
                        LogIn.oskol.getLikedTweets().remove(i);
                        reloadAndSync();
                        likeFlag[0] = isLiked(like, likedNumber, i);
                    }
                }
            });
            //adding children to Hboxes
            namesAndShits.getChildren().add(usernameLabel);
            tweetItSelf.getChildren().add(tweetText);
            likesAndShits.getChildren().addAll(likedNumber, like);
            //adding Hboxes to theTweet Vbox
            theTweet.getChildren().addAll(namesAndShits, tweetItSelf, likesAndShits);
            theTweet.setPadding(new Insets(5, 20, 5, 20));
            theTweet.setStyle(
                    "-fx-border-width: 0px 0px 1px 0px;" +
                            "-fx-border-color: #e1e8ed;"
            );
            //add theTweet Vbox in tweetLoader
            tweetLoader.getChildren().add(0, theTweet);
        }
    }

    public boolean isLiked(Button like,Label likedNumber,Tweet i){

        boolean flag1 = true;
        for (int j = 0
             ; j < LogIn.oskol.getLikedTweets().size()
                ; j++) {
            if (LogIn.oskol.getLikedTweets().get(j).getId() == i.getId()){
                like.setText("Unlike");
                like.setStyle(
                        "-fx-background-color: #ff3333;"+
                                "-fx-background-radius: 20em; "+
                                "-fx-text-fill: #ffffff;"+
                                "-fx-min-width: 50;"+
                                "-fx-font-size: 16px;"
                );
                likedNumber.setText(Integer.toString(LogIn.oskol.getLikedTweets().get(j).getLikes()));
                flag1 = false;
                break;
            }
        }
        if (flag1){
            like.setText("Like");
            like.setStyle(
                    "-fx-background-color: #1da1f2;"+
                            "-fx-background-radius: 20em; "+
                            "-fx-text-fill: #ffffff;"+
                            "-fx-min-width: 50;"+
                            "-fx-font-size: 16px;"
            );
            likedNumber.setText(Integer.toString(i.getLikes()));
        }
        return flag1;
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
        System.out.println(user);
        LogIn.oskol = gson.fromJson(user,User.class);
    }

    private void reloadAndSyncSearchedUser() {
        String json = gson.toJson(Search1.searchedUserByOskol);
        Main.clientSocket.informationsToServer.format(Protocol.toProtocol("UpdateSearchedUser","User",json));
        Main.clientSocket.informationsToServer.format("\n");
        Main.clientSocket.informationsToServer.flush();
        String user = Main.clientSocket.informationsFromServer.nextLine();
        System.out.println(user);
        Search1.searchedUserByOskol = gson.fromJson(user,User.class);
    }
}
