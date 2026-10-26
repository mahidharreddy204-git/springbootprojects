package com.example.order_service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class OrderItemResponse implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Long id;

    private Long fooditemId;

    private int quantity;

    private double price;

}
