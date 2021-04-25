package com.dmytrobilokha.treen.login.rest;

import javax.annotation.CheckForNull;

public class LoginRequest {

    @CheckForNull
    private String login;
    @CheckForNull
    private String password;
    private boolean rememberMe;

    @CheckForNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@CheckForNull String login) {
        this.login = login;
    }

    @CheckForNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@CheckForNull String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginRequest{"
                + "login='" + login + '\''
                + ", rememberMe=" + rememberMe
                + '}';
    }

}
