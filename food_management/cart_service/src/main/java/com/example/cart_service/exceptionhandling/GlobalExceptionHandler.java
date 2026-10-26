package com.example.cart_service.exceptionhandling;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    public ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status,String message){

        return ResponseEntity.status(status).body(
            Map.of(
                "timestamp",LocalDateTime.now().toString(),
                "status",status.value(),
                "error",status.getReasonPhrase(),
                "message",message
            )
        );
    }

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResource(ResourceNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    @ExceptionHandler (InsufficientStockException.class)
    public ResponseEntity<Map<String,Object>> handleStock(InsufficientStockException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler (FoodExpiredException.class)
    public ResponseEntity<Map<String,Object>> handleExpiredFood(FoodExpiredException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<Map<String,Object>> handleUnExpected(Exception ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "UnExpected error occured");
    }

}
