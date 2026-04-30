package com.backend.exceptions;

public class ServiceValidationException extends RuntimeException {
    public ServiceValidationException(String message) { super(message); }
}

