package com.restaurent.RMS.exceptionHandlers;

public class AlreadyExistException extends RuntimeException {
    public static final String ALREADY_EXISTS ="User ID Already Exists";
    public AlreadyExistException(String message) {
        super(message);
    }
}
