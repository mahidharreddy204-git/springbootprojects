package com.example.ecommerce.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(RuntimeException.class)

    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex){

        HashMap<String ,String> errors = new HashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> {
                errors.put(
                    error.getField(),
                    error.getDefaultMessage()
                );
            });

            return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST
            );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex){
        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
    }
