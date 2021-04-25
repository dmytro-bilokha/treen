package com.dmytrobilokha.treen.login.service;

import com.dmytrobilokha.treen.InternalApplicationException;

public interface AuthenticationServiceMXBean {

    void createUser(String login, String password) throws InternalApplicationException;

}
