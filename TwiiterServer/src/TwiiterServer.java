import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TwiiterServer extends Thread{
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Tweet> tweets = new ArrayList<>();
    private ServerSocket serverSocket;
    private ServerSocket notifSocket;
    ArrayList<ClientHandler> clients = new ArrayList<>();
    public static int id = 0;

    private TwiiterServer() {
        UserFileManager.loadUsers();
        loadTweets();
        try {
            this.serverSocket = new ServerSocket(9999);
            this.notifSocket = new ServerSocket(9998);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTweets() {
        if (users != null && users.size() != 0  ){
            for (User i : users) {
                tweets.addAll(i.getTweets());
            }
            Tweet[] sortedTweets = tweets.toArray(new Tweet[tweets.size()]);
            Tweet temp = new Tweet();
            boolean flag = true;
            for (int i = 0; i < tweets.size() - 1; i++) {
                for (int j = 0; j < tweets.size() - i - 1; j++) {
                    if (sortedTweets[j].getId() > sortedTweets[j+1].getId()){
                        temp = sortedTweets[j+1];
                        sortedTweets[j+1] = sortedTweets[j];
                        sortedTweets[j] = temp;
                        flag = false;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if (sortedTweets.length != 0) {
                TwiiterServer.id = sortedTweets[sortedTweets.length - 1].getId() + 1;
                System.out.println(TwiiterServer.id);
            }
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                Socket notification = notifSocket.accept();
                System.out.println("client accepted");
                ClientHandler client = new ClientHandler(socket,notification);
                client.start();
                clients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TwiiterServer twiiterServer = new TwiiterServer();
        twiiterServer.start();
    }
}
