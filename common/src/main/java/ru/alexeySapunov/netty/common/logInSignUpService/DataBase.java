package ru.alexeySapunov.netty.common.logInSignUpService;

import java.sql.*;

public class DataBase {

    private static Connection connection;
    private static Statement statement;

    public void connectBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:clientAuth.db");
        statement = connection.createStatement();
        createTable();
    }

    private static void createTable() throws SQLException {
                statement.executeUpdate("create table if not exists clientAuth (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    clientName VARCHAR(32) UNIQUE NOT NULL,\n" +
                        "    login  VARCHAR(32) UNIQUE NOT NULL,\n" +
                        "    password VARCHAR(32) UNIQUE NOT NULL);");
    }

    public String getClients(LoginClient client) throws SQLException {
        String name = null;
        if (connection != null) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT clientName FROM clientAuth WHERE login = ? AND password = ?;")) {
                ps.setString(1, client.getLog());
                ps.setString(2, client.getPass());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    name = rs.getString(1);
                } rs.close();
            }
        }
        return name;
    }

    public void getNewClients(SignUpClient client) throws SQLException {
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
