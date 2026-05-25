package com.restaurent.RMS.exceptionHandlers;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
