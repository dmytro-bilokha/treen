package com.dmytrobilokha.treen.login;

public class UserEntity {

    private final Long id;
    private final String login;
    private final byte[] passwordHash;
    private final byte[] passwordSalt;

    public UserEntity(Long id, String login, byte[] passwordHash, byte[] passwordSalt) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    @Override
    public String toString() {
        return "UserEntity{"
                + "id=" + id
                + ", login='" + login + '\''
                + '}';
    }

}
