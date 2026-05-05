package com.da.dg2526.controllers.validations;

import com.da.dg2526.exceptions.ControllerValidationException;

import java.util.Optional;

public final class ValidationResult<T> {
    private final T value;
    private final String error;

    private ValidationResult(T value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> ValidationResult<T> ok(T value) {
        return new ValidationResult<>(value, null);
    }

    public static <T> ValidationResult<T> fail(String error) {
        return new ValidationResult<>(null, error);
    }

    public boolean isInvalid() { return error != null; }

    public Optional<T> value() { return Optional.ofNullable(value); }

    public Optional<String> error() { return Optional.ofNullable(error); }

    public T getValueOrThrow() {
        if (isInvalid()) throw new ControllerValidationException("Invalid result: " + error);
        return value;
    }
}