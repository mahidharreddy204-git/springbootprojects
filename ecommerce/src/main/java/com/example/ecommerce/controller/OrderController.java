package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public Order placOrder() {   
        return orderService.placeOrder(); 
    }

    @GetMapping()
    public List<Order> getMyOrder() {
      return orderService.getMyOrder();
        }

            @GetMapping("/all")
            public List<Order> getAllOrders() {
                return orderService.getAllOrders();
            }
        
    
    

}
