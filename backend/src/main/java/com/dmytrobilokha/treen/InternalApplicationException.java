package com.dmytrobilokha.treen;

public class InternalApplicationException extends Exception {

    public InternalApplicationException(String message, Exception e) {
        super(message, e);
    }

    public InternalApplicationException(String message) {
        super(message);
    }

}
