package com.dmytrobilokha.treen.login;

import com.dmytrobilokha.treen.db.DbException;

import javax.annotation.CheckForNull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationScoped
public class UserRepository {

    private DataSource dataSource;

    public UserRepository() {
        //CDI bean
    }

    @Inject
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @CheckForNull
    public UserEntity getUserByLogin(String login) throws DbException {
        try(var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(
                    "SELECT id, login, password_hash, password_salt FROM user_data WHERE login=?")) {
            statement.setString(1, login);
            try (var resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                var userId = resultSet.getObject(1, Long.class);
                var userLogin = resultSet.getString(2);
                var userPasswordHash = resultSet.getBinaryStream(3).readAllBytes();
                var userPasswordSalt = resultSet.getBinaryStream(4).readAllBytes();
                return new UserEntity(userId, userLogin, userPasswordHash, userPasswordSalt);
            }
        } catch (SQLException | IOException e) {
            throw new DbException("Failure while trying to fetch user with login '" + login +'\'', e);
        }
    }

    public int insertUser(String login, byte[] passwordHash, byte[] passwordSalt) throws DbException {
        try(var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(
                    "INSERT INTO user_data (login, password_hash, password_salt) VALUES (?, ?, ?)")) {
            statement.setString(1, login);
            statement.setBytes(2, passwordHash);
            statement.setBytes(3, passwordSalt);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Failure while trying to store user with login '" + login +'\'', e);
        }

    }

}
