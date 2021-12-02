package ru.alexeySapunov.netty.common.dataBase;

import java.sql.*;

public class DBAuthService implements AuthService {

    private static Connection connection;
    private static Statement statement;

    public void start() {
        try {
            connectBase();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectBase();
        }
    }

    private static void connectBase() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:clientAuth.db");
            statement = connection.createStatement();
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() throws SQLException {
        statement.executeUpdate("create table if not exists clientAuth (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clientName TEXT UNIQUE," +
                "login  TEXT UNIQUE," +
                "password TEXT UNIQUE" +
                ");"
        );
    }

    public void authClients(final String login, final String password) throws SQLException {
        if (connection != null) {
            try (final PreparedStatement ps = connection.prepareStatement("SELECT clientName FROM clientAuth WHERE login = ? AND password = ?;")) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.execute();
            }
        }
    }

    public String regNewClients(final String login, final String password) throws SQLException {
        String clientName = "";
        try (final PreparedStatement ps = connection.prepareStatement("INSERT INTO clientAuth(clientName, login, password) VALUES(?, ?, ?);")) {
            ps.setString(1, clientName);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.executeUpdate();
        }
        return clientName;
    }

    private static void disconnectBase() {
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
