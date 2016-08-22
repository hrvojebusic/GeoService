package com.infobip.exception.custom;

public class PolygonNotFoundException extends RuntimeException {

    public PolygonNotFoundException() {}

    public PolygonNotFoundException(String message) {
        super(message);
    }

    public PolygonNotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
