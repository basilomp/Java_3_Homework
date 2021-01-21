package chat.auth;

import chat.User;

import java.sql.*;
import java.util.Map;

public class BaseAuthService implements AuthService{


    private static final Map<User, String> USERS = Map.of(
            new User("1", "1", "James"), "James",
            new User("2", "2", "Ulysses"), "Ulysses",
            new User("3", "3", "Finnegan"), "Finnegan"
    );

    @Override
    public void start() {
        System.out.println("Auth service started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");

    }

    @Override
    public String getNickByLoginPass(String login, String password) {
        User requestedUser = new User(login, password, null);
        return USERS.get(requestedUser);
    }

    @Override
    public boolean isExist(String nickname, String login, String password) {
        return false;
    }


}
