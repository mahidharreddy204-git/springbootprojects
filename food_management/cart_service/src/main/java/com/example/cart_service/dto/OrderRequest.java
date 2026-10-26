package com.example.cart_service.dto;

import java.util.List;

import lombok.Data;

@Data 
public class OrderRequest {
        private List<OrderItemRequest> items;


}
