package com.example.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.FoodItemResponse;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping ("/orders")
@RequiredArgsConstructor 
public class OrderController {

    private final OrderService service;

    @PostMapping("/add")
    public ResponseEntity<OrderResponse> createOrder(@RequestParam  Long userId ,@Valid  @RequestBody OrderRequest request) {
        
        return ResponseEntity.ok(service.createOrder(userId, request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        service.cancelOrder(id);

        return ResponseEntity.ok("Order Cancelled Sucessfully");
    }
    
    @GetMapping("/test-food/{id}")
    public ResponseEntity<FoodItemResponse> testFood(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.testFoodService(id)
        );
    }
    



}
