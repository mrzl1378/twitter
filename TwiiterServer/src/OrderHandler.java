import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class OrderHandler {
    public static void orderHandler(String order, String typeRepresentor, String json, ClientHandler clientHandler) {
        Gson gson = new Gson();
        switch (order) {
            case "Login":
                String userAndPass = json;
                if (typeRepresentor.equals("String[].class")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(json, "/");
                    String username = stringTokenizer.nextToken();
                    String password = stringTokenizer.nextToken();
                    login(username, password, clientHandler);
                }
                System.out.println(typeRepresentor + " " + json);
                break;
            case "CreateAccount":
                String userAndPass1 = json;
                if (typeRepresentor.equals("String[].class")) {
                    System.out.println(json);
                    StringTokenizer stringTokenizer = new StringTokenizer(json, "/");
                    String username = stringTokenizer.nextToken();
                    String password = stringTokenizer.nextToken();
                    createAccount(username, password, clientHandler);
                }
                break;
            case "Like":
                if (typeRepresentor.equals("Tweet")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(json, "/");
                    String user = stringTokenizer.nextToken();
                    String tweet = stringTokenizer.nextToken();
                    like(user, tweet, clientHandler);
                }
                break;
            case "UnLike":
                if (typeRepresentor.equals("Tweet")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(json, "/");
                    String user = stringTokenizer.nextToken();
                    String tweet = stringTokenizer.nextToken();
                    unlike(user, tweet, clientHandler);
                }
                break;
            case "UpdateUser":
                sendUser(clientHandler);
                break;
            case "Tweet":
                tweet(clientHandler, json);
                break;
            case "Search":
                searchUser(clientHandler, json);
                break;
            case "GetTimeLineTweets":
                giveTimeLineTweets(clientHandler);
                break;
            case "GetTimeLineTweetsMyProfile":
                giveTimeLineTweetsMyProfile(clientHandler);
                break;
            case "GetTimeLineTweetsProfiles":
                giveTimeLineTweetsProfiles(clientHandler);
                break;
            case "Follow":
                follow(clientHandler);
                break;
            case "UnFollow":
                unFollow(clientHandler);
                break;
            case "UpdateSearchedUser":
                sendSearchedUser(clientHandler);
                break;
        }
    }

    private static void sendSearchedUser(ClientHandler clientHandler) {
        Gson gson = new Gson();
        for (User user : TwiiterServer.users) {
            if (user.getUsername().equals(clientHandler.searchedUser[0].getUsername())) {
                System.out.println(user.getUsername());
                String jsonofUser = gson.toJson(user);
                System.out.println(jsonofUser);
                clientHandler.informationsToClient.format(jsonofUser);
                clientHandler.informationsToClient.format("\n");
                clientHandler.informationsToClient.flush();
            }
        }
    }

    private static void unFollow(ClientHandler clientHandler) {
        Gson gson = new Gson();
        for (int i = 0; i < TwiiterServer.users.size(); i++) {
            if (TwiiterServer.users.get(i).getUsername().equals(clientHandler.searchedUser[0].getUsername())) {
                clientHandler.client.getFollowings().remove(TwiiterServer.users.get(i).getUsername());
                TwiiterServer.users.get(i).getFollowers().remove(clientHandler.client.getUsername());
                System.out.println(clientHandler.client.getUsername());
                System.out.println(clientHandler.searchedUser[0].getUsername());
                break;
            }
        }
        File loggedUsers = new File("loggedUsers.txt");
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(gson.toJson(TwiiterServer.users));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void follow(ClientHandler clientHandler) {
        Gson gson = new Gson();
        for (int i = 0; i < TwiiterServer.users.size(); i++) {
            if (TwiiterServer.users.get(i).getUsername().equals(clientHandler.searchedUser[0].getUsername())) {
                clientHandler.client.getFollowings().add(TwiiterServer.users.get(i).getUsername());
                TwiiterServer.users.get(i).getFollowers().add(clientHandler.client.getUsername());
                System.out.println(clientHandler.client.getUsername());
                System.out.println(clientHandler.searchedUser[0].getUsername());
                break;
            }
        }
        File loggedUsers = new File("loggedUsers.txt");
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(gson.toJson(TwiiterServer.users));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void giveTimeLineTweetsProfiles(ClientHandler clientHandler) {
        Gson gson = new Gson();
        ArrayList<Tweet> timeLineTweets = new ArrayList<>();

        for (User user : TwiiterServer.users) {
            if (clientHandler.searchedUser[0].getUsername().equals(user.getUsername())) {
                timeLineTweets.addAll(clientHandler.searchedUser[0].getTweets());
            }
        }

        Tweet[] sortedTweets = timeLineTweets.toArray(new Tweet[timeLineTweets.size()]);
        Tweet temp = new Tweet();
        boolean flag = true;
        for (int i = 0; i < timeLineTweets.size() - 1; i++) {
            for (int j = 0; j < timeLineTweets.size() - i - 1; j++) {
                if (sortedTweets[j].getId() > sortedTweets[j + 1].getId()) {
                    temp = sortedTweets[j + 1];
                    sortedTweets[j + 1] = sortedTweets[j];
                    sortedTweets[j] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }

        String json = gson.toJson(sortedTweets);
        clientHandler.informationsToClient.format(Protocol.toProtocol("TimeLineTweets", "Tweet[]", json));
        clientHandler.informationsToClient.format("\n");
        clientHandler.informationsToClient.flush();

    }

    private static void giveTimeLineTweetsMyProfile(ClientHandler clientHandler) {
        Gson gson = new Gson();

        ArrayList<Tweet> timeLineTweets = new ArrayList<>();
        timeLineTweets.addAll(clientHandler.client.getTweets());

        Tweet[] sortedTweets = timeLineTweets.toArray(new Tweet[timeLineTweets.size()]);
        Tweet temp = new Tweet();
        boolean flag = true;
        for (int i = 0; i < timeLineTweets.size() - 1; i++) {
            for (int j = 0; j < timeLineTweets.size() - i - 1; j++) {
                if (sortedTweets[j].getId() > sortedTweets[j + 1].getId()) {
                    temp = sortedTweets[j + 1];
                    sortedTweets[j + 1] = sortedTweets[j];
                    sortedTweets[j] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }

        String json = gson.toJson(sortedTweets);
        clientHandler.informationsToClient.format(Protocol.toProtocol("TimeLineTweets", "Tweet[]", json));
        clientHandler.informationsToClient.format("\n");
        clientHandler.informationsToClient.flush();
    }

    private static void giveTimeLineTweets(ClientHandler clientHandler) {
        Gson gson = new Gson();

        ArrayList<Tweet> timeLineTweets = new ArrayList<>();
        timeLineTweets.addAll(clientHandler.client.getTweets());
        for (String str : clientHandler.client.getFollowings()) {
            for (User user : TwiiterServer.users) {
                if (str.equals(user.getUsername())) {
                    timeLineTweets.addAll(user.getTweets());
                }
            }
        }

        Tweet[] sortedTweets = timeLineTweets.toArray(new Tweet[timeLineTweets.size()]);
        Tweet temp = new Tweet();
        boolean flag = true;
        for (int i = 0; i < timeLineTweets.size() - 1; i++) {
            for (int j = 0; j < timeLineTweets.size() - i - 1; j++) {
                if (sortedTweets[j].getId() > sortedTweets[j + 1].getId()) {
                    temp = sortedTweets[j + 1];
                    sortedTweets[j + 1] = sortedTweets[j];
                    sortedTweets[j] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }

        String json = gson.toJson(sortedTweets);
        clientHandler.informationsToClient.format(Protocol.toProtocol("TimeLineTweets", "Tweet[]", json));
        clientHandler.informationsToClient.format("\n");
        clientHandler.informationsToClient.flush();

    }

    private static void searchUser(ClientHandler clientHandler, String json) {
        Gson gson = new Gson();
        boolean flag = true;
        if (TwiiterServer.users != null) {
            for (User i : TwiiterServer.users) {
                if (i.getUsername().equals(json)) {
                    clientHandler.searchedUser[0] = i;
                    flag = false;
                    clientHandler.informationsToClient.format("false");
                    clientHandler.informationsToClient.format("\n");
                    clientHandler.informationsToClient.flush();
                    String jsonUser = gson.toJson(i);
                    clientHandler.informationsToClient.format(Protocol.toProtocol("getUsers", "User", jsonUser));
                    clientHandler.informationsToClient.format("\n");
                    clientHandler.informationsToClient.flush();
                }
            }
        }
        if (flag) {
            clientHandler.informationsToClient.format("true");
            clientHandler.informationsToClient.format("\n");
            clientHandler.informationsToClient.flush();
            System.out.println(TwiiterServer.users.size());
        }
    }

    private static void tweet(ClientHandler clientHandler, String json) {
        Gson gson = new Gson();
        Tweet tweet = new Tweet(json, TwiiterServer.id, clientHandler.client.getUsername());
        TwiiterServer.tweets.add(tweet);
        clientHandler.client.getTweets().add(tweet);
        TwiiterServer.id++;
        File loggedUsers = new File("loggedUsers.txt");
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(gson.toJson(TwiiterServer.users));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unlike(String user, String tweet, ClientHandler clientHandler) {
        Gson gson = new Gson();
        Tweet tweetLikedByOskol = gson.fromJson(tweet, Tweet.class);
        int identity = tweetLikedByOskol.getId();
        for (Tweet i : TwiiterServer.tweets) {
            if (identity == i.getId()) {
                i.deLikes();
                i.getUsersLiked().remove(clientHandler.client.getUsername());
                clientHandler.client.getLikedTweets().remove(i);
                System.out.println(i.getLikes());
                break;
            }
        }
        File loggedUsers = new File("loggedUsers.txt");
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(gson.toJson(TwiiterServer.users));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendUser(ClientHandler clientHandler) {
        Gson gson = new Gson();
        String json = gson.toJson(clientHandler.client);
        clientHandler.informationsToClient.format(json);
        clientHandler.informationsToClient.format("\n");
        clientHandler.informationsToClient.flush();
    }

    private static void like(String user, String tweet, ClientHandler clientHandler) {
        Gson gson = new Gson();
        Tweet tweetLikedByOskol = gson.fromJson(tweet, Tweet.class);
        int identity = tweetLikedByOskol.getId();
        for (Tweet i : TwiiterServer.tweets) {
            if (identity == i.getId()) {
                i.setLikes();
                i.getUsersLiked().add(clientHandler.client.getUsername());
                clientHandler.client.getLikedTweets().add(i);
                System.out.println(i.getLikes());
                break;
            }
        }
        File loggedUsers = new File("loggedUsers.txt");
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(gson.toJson(TwiiterServer.users));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void login(String username, String password, ClientHandler clientHandler) {
        Gson gson = new Gson();
        boolean flag = true;
        if (TwiiterServer.users != null) {
            for (User i : TwiiterServer.users) {
                if (i.getUsername().equals(username) && i.getPassword().equals(password)) {
                    flag = false;
                    clientHandler.informationsToClient.format("false");
                    clientHandler.informationsToClient.format("\n");
                    clientHandler.informationsToClient.flush();
                    User oskol = i;
                    clientHandler.client = i;
                    String json = gson.toJson(oskol);
                    System.out.println(json);
                    clientHandler.informationsToClient.format(Protocol.toProtocol("getLoggedUsers", "User", json));
                    clientHandler.informationsToClient.format("\n");
                    clientHandler.informationsToClient.flush();
                }
            }
        }
        if (flag) {
            clientHandler.informationsToClient.format("true");
            clientHandler.informationsToClient.format("\n");
            clientHandler.informationsToClient.flush();
            System.out.println(TwiiterServer.users.size());
        }
    }

    private static void createAccount(String username, String password, ClientHandler clientHandler) {
        boolean flag = true;
        System.out.println(username + "  " + password);
        if (TwiiterServer.users != null) {
            for (User i : TwiiterServer.users) {
                if (i.getUsername().equals(username)) {
                    clientHandler.informationsToClient.format("false");
                    clientHandler.informationsToClient.format("\n");
                    clientHandler.informationsToClient.flush();
                    flag = false;
                }
            }
        }
        if (flag) {
            clientHandler.informationsToClient.format("true");
            clientHandler.informationsToClient.format("\n");
            clientHandler.informationsToClient.flush();
            User oskol = new User(username, password);
            TwiiterServer.users.add(oskol);
            clientHandler.client = oskol;
        }
        File loggedUsers = new File("loggedUsers.txt");
        Gson gson = new Gson();
        String str = gson.toJson(TwiiterServer.users);
        try {
            FileWriter fileWriter = new FileWriter(loggedUsers);
            fileWriter.write(str);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
