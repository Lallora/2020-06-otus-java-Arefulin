package ru.otus.exceptions;

public class ExcHaveNotIdField extends RuntimeException {
    public ExcHaveNotIdField(String msg){
        super(msg);
    }
}
