package ru.alexeySapunov.netty.common.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ScannerAuth extends Client {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DBAuthService dataBase = new DBAuthService();
    private static final Client client = new Client();

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

//        setName();
//        setLogin();
//        setPassword();
//
//        if (name.equals(dataBase.getClients(login, password, clientName))) {
//            System.out.println("Such name already exists, please enter new name or log in.\n" +
//                    "If you want log in, please enter LOG.\n" +
//                    "If you want continue sign up, please enter REG");
//            String str = scanner.nextLine();
//            if (str.equals("LOG")) {
//                loginClients(login, password, clientName);
//            } else {
//                regNewClients(login, password, clientName);
//            }
//        } else {
//            dataBase.signUpNewClients(log, pass, name);
//            System.out.println("Client " + getName() + " successfully registered");
//            loginClients(login, password, clientName);
//        }
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

//        setName();
//        setLogin();
//        setPassword();
//
//        if (name.equals(dataBase.getClients(login, password, clientName))) {
//            System.out.println("Welcome, " + getName());
//        } else {
//            System.out.println("Incorrect login or password, please, try again or sign up.\n" +
//                    "If you want continue log in, please enter LOG.\n" +
//                    "If you want sign up, please enter REG");
//            String str = scanner.nextLine();
//            if (str.equals("LOG")) {
//                loginClients(login, password, clientName);
//            } else {
//                regNewClients(login, password, clientName);
//            }
//        }
    }
}
