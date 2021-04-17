package com.dmytrobilokha.treen;

public class OptimisticLockException extends Exception {

    public OptimisticLockException(String message) {
        super(message);
    }

}
