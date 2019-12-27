package objectsClasses;

import java.util.ArrayList;

public class Tweet {
    private String text;
    private int id;
    private String username;
    private int likes = 0;
    private ArrayList<String> usersLiked = new ArrayList<>();
    private static boolean isRight = true;

    public Tweet() {
        this.text = null;
        this.id = 0;
        this.username = null;
    }

    public Tweet(String text, int id, String username) {
        if (text.length() <= 140){
            this.text = text;
        }else{
            isRight = false;
        }
        this.id = id;
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.length() <= 140){
            this.text = text;
        }else{
            isRight = false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes() {
        this.likes++;
    }

    public void deLikes() {
        this.likes--;
    }

    public ArrayList<String> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(ArrayList<String> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public static boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
