package ru.otus.exceptions;

public class ExcDbService extends RuntimeException {
    public ExcDbService(Exception e) {
        super(e);
    }
}
