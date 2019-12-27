package controllers;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

//singleton Class
public class ClientSocket extends Thread{
    public Socket clientSocket;
    public Socket notificationSocket;
    public Formatter informationsToServer;
    public Formatter notificationToServer;
    public Scanner informationsFromServer;
    public Scanner notificationFromServer;

    private static ClientSocket single_instance = null;
    private ClientSocket(){
        try {
            clientSocket = new Socket("localhost",9999);
            notificationSocket = new Socket("localhost",9998);
            informationsFromServer = new Scanner(clientSocket.getInputStream());
            notificationFromServer = new Scanner(notificationSocket.getInputStream());
            informationsToServer = new Formatter(clientSocket.getOutputStream());
            notificationToServer = new Formatter(notificationSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClientSocket getInstance()
    {
        if (single_instance == null)
            single_instance = new ClientSocket();

        return single_instance;
    }

    @Override
    public void run(){
        while(true){
            //nothing
        }
    }
}
