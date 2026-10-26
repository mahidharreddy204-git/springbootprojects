package com.example.order_service.service;


import com.example.order_service.dto.FoodItemResponse;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(Long userId,OrderRequest request);


    OrderResponse getOrderById(Long id);

    void cancelOrder(Long id);

    FoodItemResponse testFoodService(Long id);


}
