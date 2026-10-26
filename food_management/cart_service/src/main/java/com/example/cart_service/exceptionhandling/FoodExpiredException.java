package com.example.cart_service.exceptionhandling;

public class FoodExpiredException extends RuntimeException{

    public FoodExpiredException(String message){
        super(message);
    }

}
