package com.example.cart_service.exceptionhandling;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message){
        super(message);
    }

}
