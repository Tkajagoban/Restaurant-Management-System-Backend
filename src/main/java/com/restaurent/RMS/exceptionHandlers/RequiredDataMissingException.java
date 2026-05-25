package com.restaurent.RMS.exceptionHandlers;

public class RequiredDataMissingException extends RuntimeException {
    public RequiredDataMissingException(String message) {
        super(message);
    }
}
