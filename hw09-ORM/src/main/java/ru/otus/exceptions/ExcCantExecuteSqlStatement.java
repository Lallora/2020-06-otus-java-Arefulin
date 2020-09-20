package ru.otus.exceptions;

public class ExcCantExecuteSqlStatement extends RuntimeException{
    public ExcCantExecuteSqlStatement(Exception e){
        super(e);
    }
}
