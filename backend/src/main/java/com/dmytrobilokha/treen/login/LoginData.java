package com.dmytrobilokha.treen.login;

import java.io.Serializable;

public class LoginData implements Serializable {

    private final long userId;
    private final String login;

    public LoginData(long userId, String login) {
        this.userId = userId;
        this.login = login;
    }

    public long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "LoginData{"
                + "userId=" + userId
                + ", login='" + login + '\''
                + '}';
    }
}
