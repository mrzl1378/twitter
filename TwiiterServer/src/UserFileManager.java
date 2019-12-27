import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserFileManager {
    public static void loadUsers() {
        File loggedUsers = new File("loggedUsers.txt");
        if (loggedUsers.exists()) {
            try {
                FileReader fileReader = new FileReader(loggedUsers);
                String json = "";
                int chCode;
                while (-1 != (chCode = fileReader.read())) {
                    json += (char) (chCode);
                }
                fileReader.close();
                System.out.println(json);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<User>>() {
                }.getType();
                TwiiterServer.users = gson.fromJson(json, type);
                if (TwiiterServer.users == null) {
                    TwiiterServer.users = new ArrayList<>();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter fileWriter = new FileWriter(loggedUsers);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
