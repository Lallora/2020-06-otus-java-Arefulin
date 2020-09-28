package ru.otus.exceptions;

public class ExcMapperException  extends RuntimeException {

    public ExcMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcMapperException(String message) {
        super(message);
    }
}
