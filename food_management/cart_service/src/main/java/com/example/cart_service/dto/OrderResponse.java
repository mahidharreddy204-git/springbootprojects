package com.example.cart_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data 
public class OrderResponse {
      private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;
    private List<OrderItemResponse> items;

}
