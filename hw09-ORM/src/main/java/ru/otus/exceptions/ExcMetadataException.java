package ru.otus.exceptions;

public class ExcMetadataException extends RuntimeException {
    public ExcMetadataException(String message) {
        super(message);
    }

    public ExcMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
