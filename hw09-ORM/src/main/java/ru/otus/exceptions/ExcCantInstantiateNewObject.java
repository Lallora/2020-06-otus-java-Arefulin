package ru.otus.exceptions;

/**
 * When new object can't be instantiated by reflection api throught a Constructor.newInstance
 * when sql ResultSet maps to java object this exception must be thrown
 */
public class ExcCantInstantiateNewObject extends RuntimeException {
    public ExcCantInstantiateNewObject(){
    }

    public ExcCantInstantiateNewObject(Exception e){
        super(e);
    }
}
