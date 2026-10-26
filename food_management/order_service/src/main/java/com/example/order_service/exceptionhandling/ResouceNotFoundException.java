package com.example.order_service.exceptionhandling;

public class ResouceNotFoundException  extends RuntimeException{
    

    public ResouceNotFoundException (String message){
        super(message);
    }


}
