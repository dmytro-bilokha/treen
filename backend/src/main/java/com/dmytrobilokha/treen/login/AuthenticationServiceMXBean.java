package com.dmytrobilokha.treen.login;

import com.dmytrobilokha.treen.InternalApplicationException;

public interface AuthenticationServiceMXBean {

    void createUser(String login, String password) throws InternalApplicationException;

}
