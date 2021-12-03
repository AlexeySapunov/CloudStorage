package ru.alexeySapunov.netty.common.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ScannerAuth extends DBClient {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DBAuthService dataBase = new DBAuthService();
    private static final DBClient client = new DBClient();

    public void signUpNewClients() throws SQLException {

        System.out.println("Welcome, please sign up");

        String login = scanner.nextLine();
        String password = scanner.nextLine();

        if (!login.equals("") && password.equals("")) {
            loginClients(login, password);
        } else {
            System.out.println("Please, log in");
        }

        try {
            dataBase.getNewClients(client);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void loginClients(String login, String password) throws SQLException {

        System.out.println("Welcome, please log in");

        client.setLog(login);
        client.setPass(password);
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
        }
    }
}
