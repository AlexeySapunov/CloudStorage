package ru.alexeySapunov.netty.common.logInSignUpService;

import ru.alexeySapunov.netty.common.message.Message;

public class SignUpClient extends Message {

    private String name;
    private String log;
    private String pass;

    public SignUpClient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
