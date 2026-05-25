package com.restaurent.RMS.exceptionHandlers;

public class AccessRevokedException extends RuntimeException {
    public AccessRevokedException(String message) {
        super(message);
    }
}
