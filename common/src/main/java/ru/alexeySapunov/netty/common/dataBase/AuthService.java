package ru.alexeySapunov.netty.common.dataBase;

import java.sql.SQLException;

public interface AuthService {
    void authClients(final String login, final String password) throws SQLException;
    String regNewClients(final String login, final String password) throws SQLException;
}
