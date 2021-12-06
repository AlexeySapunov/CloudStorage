package ru.alexeySapunov.netty.common.logInSignUpService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginClients extends DBClient {

    public LoginClients(String log, String pass) throws SQLException {

        DBClient client = new DBClient();
        DBAuthService dataBase = new DBAuthService();

        client.setLog(log);
        client.setPass(pass);
        ResultSet result = dataBase.getClients(client);

        int counter = 0;

        try {
            while (result.next()) {
                counter++;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        if (counter >= 1) {
            System.out.println("Client " + client.getName() + " logged in successfully");
        } else {
            System.out.println("Incorrect login or password");
        }
    }
}
