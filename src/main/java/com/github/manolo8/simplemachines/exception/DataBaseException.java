package com.github.manolo8.simplemachines.exception;

public class DataBaseException extends Exception {

    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(Exception e) {
        super(e);
    }
}
