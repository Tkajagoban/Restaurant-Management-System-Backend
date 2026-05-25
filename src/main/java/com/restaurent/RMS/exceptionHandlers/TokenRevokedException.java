package com.restaurent.RMS.exceptionHandlers;

public class TokenRevokedException extends RuntimeException {
    public TokenRevokedException(String message) {
        super(message);
    }
}
