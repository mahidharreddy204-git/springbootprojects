package com.example.cart_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.cart_service.dto.FoodItemResponse;

@FeignClient (name="food-service")
public interface FoodServiceClient {

    @GetMapping ("/item/get/{id}")
    FoodItemResponse getFoodItemById(@PathVariable("id") Long id);
}
