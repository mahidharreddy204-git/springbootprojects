package com.example.order_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.order_service.dto.FoodItemResponse;


@FeignClient (name="food-service")
public interface FoodServiceClients {

    @GetMapping("/item/get/{id}")
    FoodItemResponse getFoodItemById(@PathVariable ("id") Long id);

    @PutMapping("/item/reduce-stock/{id}")
    void reduceStock(@PathVariable ("id") Long id, @RequestParam ("quantity") int quantity);

}
