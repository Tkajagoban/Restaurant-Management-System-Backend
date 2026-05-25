package com.restaurent.RMS.exceptionHandlers;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
