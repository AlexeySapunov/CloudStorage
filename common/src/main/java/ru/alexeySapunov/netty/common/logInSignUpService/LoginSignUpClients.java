package ru.alexeySapunov.netty.common.logInSignUpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class LoginSignUpClients extends DBClient {

    public void loginClients() throws SQLException {
        System.out.println("Welcome, please login");

        DBClient client = new DBClient();
        DBAuthService dataBase = new DBAuthService();

        try (var bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            dataBase.connectBase();

            System.out.println("Enter your login: ");
            String log = bufferedReader.readLine();

            System.out.println("Enter your password: ");
            String password = bufferedReader.readLine();

            client.setLog(log);
            client.setPass(password);
            dataBase.getClients(client);

            if (log.equals(dataBase.getLog())) {
                System.out.println("Client " + client.getName() + " logged in successfully");
            } else {
                System.out.println("Incorrect log or password, please sign up");
                signUpClients();
            }

        } catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
        } finally {
            dataBase.disconnectBase();
        }
    }

    public void signUpClients() throws SQLException {

        DBClient client = new DBClient();
        DBAuthService dataBase = new DBAuthService();

        try (var bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            dataBase.connectBase();

            System.out.println("Enter your name: ");
            String name = bufferedReader.readLine();

            System.out.println("Enter your login: ");
            String log = bufferedReader.readLine();

            System.out.println("Enter your password: ");
            String password = bufferedReader.readLine();

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
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnectBase();
        }
    }
}
