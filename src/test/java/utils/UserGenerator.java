package utils;

public class UserGenerator {

    public static User getValidUser() {
        String name = "Иван_" + System.currentTimeMillis();
        String email = "ivan" + System.currentTimeMillis() + "@example.com";
        String password = "Password123";
        return new User(name, email, password);
    }
}
