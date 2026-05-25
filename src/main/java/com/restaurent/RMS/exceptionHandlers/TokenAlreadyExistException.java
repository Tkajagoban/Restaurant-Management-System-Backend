package com.restaurent.RMS.exceptionHandlers;

public class TokenAlreadyExistException extends RuntimeException {
    public TokenAlreadyExistException(String message) {
        super(message);
    }
}
