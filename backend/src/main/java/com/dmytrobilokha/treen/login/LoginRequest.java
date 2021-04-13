package com.dmytrobilokha.treen.login;

import javax.annotation.CheckForNull;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class LoginRequest {

    @CheckForNull
    private final String login;
    @CheckForNull
    private final String password;
    private final boolean rememberMe;

    @JsonbCreator
    public LoginRequest(@CheckForNull @JsonbProperty("login") String login,
                        @CheckForNull @JsonbProperty("password") String password,
                        @JsonbProperty("rememberMe") boolean rememberMe) {
        this.login = login;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    @CheckForNull
    public String getLogin() {
        return login;
    }

    @CheckForNull
    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    @Override
    public String toString() {
        return "LoginRequest{"
                + "login='" + login + '\''
                + ", rememberMe=" + rememberMe
                + '}';
    }

}
