package ru.alexeySapunov.netty.common.logInSignUpService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginSignUpClients extends DBClient {

    public void loginClients() throws SQLException {
        try {
            DBClient client = new DBClient();
            DBAuthService dataBase = new DBAuthService();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your login: ");
            String log = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            scanner.close();

            client.setLog(log);
            client.setPass(password);
            ResultSet result = dataBase.getClients(client);

            int counter = 0;

            if (result.next()) {
                counter++;
            }

            if (counter >= 1) {
                System.out.println("Client " + client.getName() + " logged in successfully");
            } else {
                System.out.println("Incorrect log or password, please sign up");
                signUpClients();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void signUpClients() throws SQLException {

        DBClient client = new DBClient();
        DBAuthService dataBase = new DBAuthService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Enter your login: ");
        String log = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        scanner.close();

        client.setName(name);
        client.setLog(log);
        client.setPass(password);

        if (name.equals(dataBase.getName())) {
            System.out.println("This client name " + client.getName() + " is busy, please login");
            loginClients();
        } else {
            dataBase.getNewClients(client);
            System.out.println("Client " + client.getName() + " is registered");
        }
    }
}
