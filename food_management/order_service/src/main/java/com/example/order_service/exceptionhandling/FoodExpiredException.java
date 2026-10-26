package com.example.order_service.exceptionhandling;

public class FoodExpiredException extends RuntimeException {

    public FoodExpiredException(String message){
        super(message);
    }

}
