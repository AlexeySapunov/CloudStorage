package ru.alexeySapunov.netty.common.logInSignUpService;

public class DBClient {
    private String name;
    private String log;
    private String pass;

    public DBClient(String name, String log, String pass) {
        this.name = name;
        this.log = log;
        this.pass = pass;
    }

    public DBClient() {
    }

    public String getName() {
        return name;
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
