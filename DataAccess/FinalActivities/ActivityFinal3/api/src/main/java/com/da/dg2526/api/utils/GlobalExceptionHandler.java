package com.da.dg2526.api.utils;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems") // Cannot do anything with warnings.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // I got annoyed that I could not control in any way what Validation actually spits out.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidation(MethodArgumentNotValidException e) {
        var errorsList = new ArrayList<String>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            String s = error.getField() + ": " + error.getDefaultMessage();
            errorsList.add(s);
        }

        return ResponseEntity.badRequest().body(errorsList);
    }

    //Service exceptions.
    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<String> handleServiceException(ServiceValidationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
