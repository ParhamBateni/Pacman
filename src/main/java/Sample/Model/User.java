package Sample.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {
    public static User loggedInUser;
    public static User notLoggedInUser = new User("not user", "");
    private static ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Map> savedMaps = new ArrayList<>();
    private Map selectedMap;
    private String username;
    private String password;
    private int maxScore;


    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
        if (!username.equals("not user"))
            users.add(this);
        savedMaps.add(Map.map1);
        savedMaps.add(Map.map2);
        savedMaps.add(Map.map3);
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void setLoggedInUser(User loggedInUser) {
        User.loggedInUser = loggedInUser;
    }

    public static void setAllUsers(ArrayList<User> dataBaseUsers) {
        users = dataBaseUsers;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static ArrayList<User> get10MaxScoreUsers() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.maxScore > o2.maxScore) {
                    return -1;
                } else if (o1.maxScore == o2.maxScore) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return users;
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

    public void removeUser() {
        users.remove(this);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int score) {
        if (maxScore < score) {
            maxScore = score;
        }
    }

    public void addMapToSavedMaps(Map map) {
        savedMaps.add(map);
    }

    public ArrayList<Map> getSavedMaps() {
        return savedMaps;
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(Map selectedMap) {
        this.selectedMap = selectedMap;
    }
}
