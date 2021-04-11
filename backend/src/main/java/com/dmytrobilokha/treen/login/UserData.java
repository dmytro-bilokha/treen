package com.dmytrobilokha.treen.login;

import javax.annotation.CheckForNull;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class UserData implements Serializable {

    @CheckForNull
    private String login;

    @CheckForNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@CheckForNull String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserData{"
                + "login='" + login + '\''
                + '}';
    }

}
