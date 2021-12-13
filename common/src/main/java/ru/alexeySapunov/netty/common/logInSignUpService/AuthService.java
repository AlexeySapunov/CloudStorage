package ru.alexeySapunov.netty.common.logInSignUpService;

import ru.alexeySapunov.netty.common.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AuthService extends Message {

    public LoginClient login() throws IOException {
        System.out.println("Welcome, please log in!");

        LoginClient loginClient = new LoginClient();

        BufferedReader loginReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter your name, please: ");
        String name = loginReader.readLine();
        loginClient.setName(name);

        System.out.println("Enter your log, please: ");
        String log = loginReader.readLine();
        loginClient.setLog(log);

        System.out.println("Enter your pass, please: ");
        String pass = loginReader.readLine();
        loginClient.setPass(pass);

        return loginClient;
    }

    public SignUpClient signUp() throws IOException {
        System.out.println("Welcome, please sign up!");

        SignUpClient signUpClient = new SignUpClient();

        BufferedReader signUpReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter your name, please: ");
        String name = signUpReader.readLine();
        signUpClient.setName(name);

        System.out.println("Enter your log, please: ");
        String log = signUpReader.readLine();
        signUpClient.setLog(log);

        System.out.println("Enter your pass, please: ");
        String pass = signUpReader.readLine();
        signUpClient.setPass(pass);

        return signUpClient;
    }
}
