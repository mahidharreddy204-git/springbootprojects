package com.example.order_service.exceptionhandling;


public class OrderCancellationException extends RuntimeException {

    public OrderCancellationException(String message){
        super(message);
    }

}
