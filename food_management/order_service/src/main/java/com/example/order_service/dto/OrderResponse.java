package com.example.order_service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.example.order_service.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class OrderResponse implements Serializable{


    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private LocalDateTime orderDate;

    private double totalAmount;

    private OrderStatus status;

    private List<OrderItemResponse> items;

}
