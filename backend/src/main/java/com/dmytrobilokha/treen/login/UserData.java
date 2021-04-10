package com.dmytrobilokha.treen.login;

public class UserData {

    private final String login;

    public UserData(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "UserData{"
                + "login='" + login + '\''
                + '}';
    }

}
