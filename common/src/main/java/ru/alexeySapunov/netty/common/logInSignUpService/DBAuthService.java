package ru.alexeySapunov.netty.common.logInSignUpService;

import java.sql.*;

public class DBAuthService extends DBClient {

    private static Connection connection;
    private static Statement statement;

    public void connectBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:clientAuth.db");
        statement = connection.createStatement();
        createTable();
    }

    private static void createTable() throws SQLException {
        statement.executeUpdate("create table if not exists clientAuth (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clientName VARCHAR(32) UNIQUE NOT NULL," +
                "login  VARCHAR(32) UNIQUE NOT NULL," +
                "password VARCHAR(32) UNIQUE NOT NULL" +
                ");"
        );
    }

    public ResultSet getClients(DBClient client) throws SQLException {
        ResultSet rs = null;
        if (connection != null) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM clientAuth WHERE login = ? AND password = ?;")) {
                ps.setString(1, client.getLog());
                ps.setString(2, client.getPass());
                rs = ps.executeQuery();
            }
        }
        return rs;
    }

    public void getNewClients(DBClient client) throws SQLException {
        if (connection != null) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO clientAuth(clientName, login, password) VALUES(?, ?, ?);")) {
                ps.setString(1, client.getName());
                ps.setString(2, client.getLog());
                ps.setString(3, client.getPass());

                ps.executeUpdate();
            }
        }
    }

    public void disconnectBase() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
