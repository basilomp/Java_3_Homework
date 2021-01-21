package chat.auth;

import chat.handler.DBHandler;

public class DBAuthService implements AuthService{
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getNickByLoginPass(String login, String password) {
        return DBHandler.getNickByLoginAndPassword(login, password);
    }

    @Override
    public boolean isExist(String nickname, String login, String password) {
        return false;
    }
}
