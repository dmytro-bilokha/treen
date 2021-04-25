package com.dmytrobilokha.treen.login.persistence;

import com.dmytrobilokha.treen.db.DbException;
import com.dmytrobilokha.treen.db.DbQueryExecutor;

import javax.annotation.CheckForNull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserRepository {

    private DbQueryExecutor queryExecutor;

    public UserRepository() {
        //CDI bean
    }

    @Inject
    public UserRepository(DbQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @CheckForNull
    public UserEntity findUserByLogin(String login) throws DbException {
        return queryExecutor.selectObject(new FindUserByLoginQuery(login));
    }

    @CheckForNull
    public Long insertUser(String login, byte[] passwordHash, byte[] passwordSalt) throws DbException {
        return queryExecutor.insert(new InsertUserQuery(login, passwordHash, passwordSalt));
    }

}
