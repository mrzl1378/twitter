import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler extends Thread {
    Socket connection;
    Socket notification;
    Formatter informationsToClient;
    Formatter notificitionToClient;
    Scanner notificationFromClient;
    Scanner informatonsFromClient;
    User client;
    User[] searchedUser = new User[2];
    public ClientHandler(Socket connection , Socket notification){
        this.connection = connection;
        this.notification = notification;
        try {
            informationsToClient = new Formatter(connection.getOutputStream());
            informatonsFromClient = new Scanner(connection.getInputStream());
            notificitionToClient = new Formatter(notification.getOutputStream());
            notificationFromClient = new Scanner(notification.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while (connection.isConnected() && !connection.isClosed()){
//            if (!connection.isClosed() && connection.isConnected()) {
                messageReader(informatonsFromClient.nextLine(), this);
//            }
        }
    }

    private void messageReader(String nextLine ,ClientHandler clientHandler) {
        System.out.println(nextLine);
        StringTokenizer stringTokenizer = new StringTokenizer(nextLine,"&");
        String order = stringTokenizer.nextToken();
        String typeRepresentor = stringTokenizer.nextToken();
        String gson = stringTokenizer.nextToken();
        OrderHandler.orderHandler(order,typeRepresentor,gson,clientHandler);
    }
}
