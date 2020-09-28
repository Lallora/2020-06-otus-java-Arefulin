package ru.otus.exceptions;

public class ExcUserDao extends RuntimeException {
    public ExcUserDao(Exception ex) {
        super(ex);
    }
}
