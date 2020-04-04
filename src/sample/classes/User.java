package sample.classes;

public class User {

    static String username;
    static String name;
    static String type;

    public static void setName(String name) {
        User.name = name;
    }

    public static String getName() {
        return name;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        User.type = type;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

}
