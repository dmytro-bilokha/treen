package com.dmytrobilokha.treen.login;

import javax.annotation.CheckForNull;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class UserSessionData implements Serializable {

    @CheckForNull
    private volatile LoginData loginData;

    public void registerLogin(LoginData loginData) {
        this.loginData = loginData;
    }

    public void registerLogout() {
        this.loginData = null;
    }

    @CheckForNull
    public LoginData getLoginData() {
        return loginData;
    }

}
