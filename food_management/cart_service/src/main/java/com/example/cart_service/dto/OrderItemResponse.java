package com.example.cart_service.dto;

import lombok.Data;

@Data 
public class OrderItemResponse {
    private Long id;
    private Long foodItemId;
    private int quantity;
    private double price;

}
