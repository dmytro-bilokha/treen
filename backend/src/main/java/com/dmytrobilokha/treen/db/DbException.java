package com.dmytrobilokha.treen.db;

public class DbException extends Exception {

    public DbException(String message, Exception e) {
        super(message, e);
    }

}
