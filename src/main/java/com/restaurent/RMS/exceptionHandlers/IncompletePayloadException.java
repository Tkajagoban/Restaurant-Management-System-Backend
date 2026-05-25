package com.restaurent.RMS.exceptionHandlers;

public class IncompletePayloadException extends RuntimeException {
    public IncompletePayloadException(String message) {
        super(message);
    }
}
