package ru.alexeySapunov.netty.common.dataBase;

import java.sql.*;

public class DBAuthService implements AuthService {

    private static final String INSERT_NEW_USER = "INSERT INTO clients(login, password) VALUES(?, ?);";
    private static final String URL = "jdbc:sqlite:clientAuth.db";

    private static Connection connection;
    private static Statement statement;

    public void connectBase() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(URL);
        statement = connection.createStatement();
    }

    private static void createTable() throws SQLException {
        statement.executeUpdate("create table if not exists clientAuth (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "login  TEXT," +
                "pass TEXT"+
                ")");

    }

    public void insertNewClients(final String login, final String password) throws SQLException {
        try (final PreparedStatement ps = connection.prepareStatement(INSERT_NEW_USER)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.executeUpdate();
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
