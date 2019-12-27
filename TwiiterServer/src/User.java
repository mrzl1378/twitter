import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String name;
    //    private String email;
//    private String bio;
//    private int phoneNumber;
//    private boolean doneLike = false;
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> followings = new ArrayList<>();
    private ArrayList<Tweet> likedTweets = new ArrayList<>();
    private ArrayList<Tweet> Tweets = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }

    public ArrayList<Tweet> getLikedTweets() {
        return likedTweets;
    }

    public void setLikedTweets(ArrayList<Tweet> likedTweets) {
        this.likedTweets = likedTweets;
    }

    public ArrayList<Tweet> getTweets() {
        return Tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        Tweets = tweets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    public void follow(String kelash){
        for (int i = 0; i < TwiiterServer.users.size(); i++) {
            if (kelash.equals(TwiiterServer.users.get(i))){
                this.followings.add(TwiiterServer.users.get(i).getUsername());
                TwiiterServer.users.get(i).getFollowers().add(this.getUsername());
                break;
            }
        }
    }

    public void unfollow(User kelash){
        for (int i = 0; i < this.getFollowings().size(); i++) {
            if (kelash.username.equals(this.getFollowings().get(i))){
                this.getFollowings().remove(i);
            }
        }

        for (int i = 0; i < kelash.getFollowers().size(); i++) {
            if (this.getUsername().equals(kelash.getFollowers().get(i))){
                kelash.getFollowers().remove(i);
            }
        }
    }

    public void logOut(){

    }
}