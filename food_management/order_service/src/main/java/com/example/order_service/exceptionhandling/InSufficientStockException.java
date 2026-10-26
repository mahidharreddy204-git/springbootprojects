package com.example.order_service.exceptionhandling;

public class InSufficientStockException extends  RuntimeException{

    public InSufficientStockException(String message){
        super(message);
    }

}
