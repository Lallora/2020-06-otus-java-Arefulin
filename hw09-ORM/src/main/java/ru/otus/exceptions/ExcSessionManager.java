package ru.otus.exceptions;


public class ExcSessionManager extends RuntimeException {
    public ExcSessionManager(String msg) {
        super(msg);
    }

    public ExcSessionManager(Exception ex) {
        super(ex);
    }
}
