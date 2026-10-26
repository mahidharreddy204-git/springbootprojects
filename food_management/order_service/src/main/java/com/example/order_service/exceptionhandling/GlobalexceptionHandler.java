package com.example.order_service.exceptionhandling;

import java.time.LocalDateTime;
import java.util.Map;

import javax.naming.InsufficientResourcesException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalexceptionHandler {

    public ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status , String message){
        return ResponseEntity.status(status).body(
            Map.of(
                "timestamp",LocalDateTime.now().toString(),
                "status",status.value(),
                "error",status.getReasonPhrase(),
                "message",message
            )
        );
    }

    @ExceptionHandler (ResouceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(ResouceNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler (InSufficientStockException.class)
    public ResponseEntity<Map<String,Object>> handleInsufficientStock(InsufficientResourcesException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler (OrderCancellationException.class)
    public ResponseEntity<Map<String,Object>> handleOrderException(OrderCancellationException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler (FoodExpiredException.class)
    public ResponseEntity<Map<String,Object>> handleExpiredFood(FoodExpiredException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
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

}
