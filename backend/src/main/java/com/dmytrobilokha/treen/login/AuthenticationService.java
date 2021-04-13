package com.dmytrobilokha.treen.login;

import com.dmytrobilokha.treen.InternalApplicationException;
import com.dmytrobilokha.treen.db.DbException;

import javax.annotation.CheckForNull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = DbException.class)
public class AuthenticationService {

    private UserRepository userRepository;

    public AuthenticationService() {
        //CDI
    }

    @Inject
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CheckForNull
    public UserEntity findUserByCredentials(String login, char[] password) throws InternalApplicationException {
        UserEntity userEntity;
        try {
            userEntity = userRepository.getUserByLogin(login);
        } catch (DbException e) {
            throw new InternalApplicationException("Db error while authenticating user", e);
        }
        return userEntity;
    }

}
