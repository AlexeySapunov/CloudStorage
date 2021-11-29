package ru.alexeySapunov.netty.common.dataBase;

import java.sql.SQLException;
import java.util.Scanner;

public class ScannerAuth implements AuthService {

    private static final Scanner scanner = new Scanner(System.in);

    private static String name;
    private static String login;
    private static String password;

    public String regNewClients(final String login, final String password) throws SQLException {

        while (true) {
            setName();
            setLogin();
            setPassword();

            if (name.equals(new DBAuthService().regNewClients(login, password))) {
                System.out.println("Such name already exists, please enter new name");
                regNewClients(login, password);
            } else {
                System.out.println("Client " + getName() + " successfully registered");
                new DBAuthService().regNewClients(login, password);
            }
        }
    }

    public void authClients(final String login, final String password) throws SQLException {

        while (true) {
            setName();
            setLogin();
            setPassword();

            if (name.equals(new DBAuthService().regNewClients(login, password))) {
                System.out.println("Welcome, " + getName());
                new DBAuthService().authClients(login, password);
            } else {
                System.out.println("Incorrect login or password, please, sign up");
                regNewClients(login, password);
            }
        }
    }

    private static void setName() {
        System.out.println("Enter your name: ");
        name = scanner.nextLine();
    }

    private static void setLogin() {
        System.out.println("Enter your login: ");
        ScannerAuth.login = scanner.nextLine();
    }

    private static void setPassword() {
        System.out.println("Enter your password: ");
        ScannerAuth.password = scanner.nextLine();
    }

    private static String getName() {
        return name;
    }
}
