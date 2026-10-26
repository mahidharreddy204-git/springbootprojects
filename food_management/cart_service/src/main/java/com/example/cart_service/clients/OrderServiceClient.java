package com.example.cart_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cart_service.dto.OrderRequest;
import com.example.cart_service.dto.OrderResponse;

@FeignClient (name="order-service")
public interface OrderServiceClient {

    @PostMapping("/orders/add")
    OrderResponse createOrder(
            @RequestParam("userId") Long userId,
            @RequestBody OrderRequest request
    );

}
