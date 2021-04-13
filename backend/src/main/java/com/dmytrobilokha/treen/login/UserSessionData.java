package com.dmytrobilokha.treen.login;

import javax.annotation.CheckForNull;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

@SessionScoped
public class UserSessionData implements Serializable {

    private final AtomicReference<LoginData> loginData = new AtomicReference<>();

    public void registerLogin(LoginData loginData) {
        this.loginData.set(loginData);
    }

    public void registerLogout() {
        this.loginData.set(null);
    }

    @CheckForNull
    public LoginData getLoginData() {
        return loginData.get();
    }

}
