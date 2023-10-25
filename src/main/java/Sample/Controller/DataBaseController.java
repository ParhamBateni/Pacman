package Sample.Controller;

import Sample.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

public class DataBaseController {
    public static void writeJSON(Object object, String fileAddress) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        writeFile(fileAddress, gson.toJson(object));
    }


    public static void writeFile(String fileAddress, String content) throws IOException {
        FileWriter writer = new FileWriter(fileAddress);
        writer.write(content);
        writer.close();
    }

    public static void usersDataBaseInitialization() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        File directoryPath = new File("src\\main\\resources\\DataBase\\users\\");
        File[] filesList = directoryPath.listFiles();
        ArrayList<User> dataBaseUsers = new ArrayList<>();
        for (File file : filesList) {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(file.getPath())
            );
            User user = gson.fromJson(bufferedReader, User.class);
            dataBaseUsers.add(user);
        }
        User.setAllUsers(dataBaseUsers);
    }


    public static void saveUsersData() throws Exception {
        File directory = new File("src\\main\\resources\\DataBase\\users\\");
        for (File file : directory.listFiles()) {
            file.delete();
        }
        for (User user : User.getUsers()) {
            writeJSON(user, "src\\main\\resources\\DataBase\\users\\" + user.getUsername() + ".json");
        }
    }
}
