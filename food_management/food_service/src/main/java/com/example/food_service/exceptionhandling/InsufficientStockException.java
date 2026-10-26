package com.example.food_service.exceptionhandling;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message){
        super(message);
    }

}
