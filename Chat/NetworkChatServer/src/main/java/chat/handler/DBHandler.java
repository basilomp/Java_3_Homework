package chat.handler;

import java.sql.*;

public class DBHandler {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement getNickFromDB;

//    public static void main(String[] args) {
//        connectDB();
//    }

    public static boolean connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            prepareAllStatements();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException {
        getNickFromDB = connection.prepareStatement("SELECT nickname FROM users where login = ? and password = ?;");
    }

    public static String getNickByLoginAndPassword(String login, String password) {
        String nickname = null;
        try {
           getNickFromDB.setString(1, login);
           getNickFromDB.setString(2, password);
           ResultSet getResult = getNickFromDB.executeQuery();
           if (getResult.next()) {
               nickname = getResult.getString(1);
           }
           getResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nickname;
    }

    public static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
