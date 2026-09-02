package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entity.Order;

public interface OrderService {

    Order placeOrder();

    List<Order> getMyOrder();

    List<Order> getAllOrders();


}
