package com.example.food_service.exceptionhandling;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice 
public class GlobalExceptionHandling {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("timestamp",LocalDateTime.now().toString(),
                        "status",404,
                        "error","Not Found",
                        "message",ex.getMessage())
            );

    }

    @ExceptionHandler (InsufficientStockException.class)
    public ResponseEntity<Map<String,Object>> handleInsufficientStock(InsufficientStockException ex){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of(
                "timestamp",LocalDateTime.now().toString(),
                "status", 409,
                "error","Conflict",
                "message",ex.getMessage()

            )
        );
    }

     @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {

    ex.printStackTrace();

    return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage() != null
                    ? ex.getMessage()
                    : "An unexpected error occurred"
    );
}
private ResponseEntity<Map<String, Object>> buildResponse(
        HttpStatus status, String message) {

    return ResponseEntity.status(status).body(
            Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", status.value(),
                    "error", status.getReasonPhrase(),
                    "message", message
            )
    );
}
}
