package ru.otus.exceptions;

/**
 * Class for mapping must has one field which is annotated by {@link Id}
 * If class has more than one id annotated field this exception must be thrown
 */
public class ExcIdFieldMustBeOnlyOne extends RuntimeException {
}
