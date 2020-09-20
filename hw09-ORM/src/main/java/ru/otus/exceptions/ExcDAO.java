package ru.otus.exceptions;

public class ExcDAO extends RuntimeException {
    public ExcDAO(String message) {
        super(message);
    }

    public ExcDAO() {
        super();
    }

    public ExcDAO(Exception e) {
        super(e);
    }
}
