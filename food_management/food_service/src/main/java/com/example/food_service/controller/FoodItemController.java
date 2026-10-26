package com.example.food_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.food_service.dto.FoodItemRequest;
import com.example.food_service.dto.FoodItemResponse;
import com.example.food_service.service.FoodItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping ("/item")
@RequiredArgsConstructor 
public class FoodItemController {

    @Value("${food.service.secret}")
    private String serviceSecret;



    private final FoodItemService service;

    @PostMapping("/add")
    public FoodItemResponse addFoodItem(@RequestBody FoodItemRequest request ) {
        return service.createFoodItem(request);
    }

    @GetMapping("/get")
    public List<FoodItemResponse> getAllFoodItems() {
        return service.getAllFoodItems();
    }

    @GetMapping("/get/{id}")
    public FoodItemResponse getItemById(@PathVariable Long id) {
        return service.getFoodItemById(id);
    }

    @PutMapping("/update/{id}")
    public FoodItemResponse updateFoodItem(@PathVariable Long id, @RequestBody FoodItemRequest request) {
       
        return service.updateFoodItem(id, request);
    }
    
    @DeleteMapping("/del/{id}")
    public void deleteFoodItem(@PathVariable Long id){
         service.deleteFoodItem(id);
    }

            @PutMapping("/reduce-stock/{id}")
            public ResponseEntity<String> reduceStock(
                    @PathVariable Long id,
                    @RequestParam int quantity,
                    @RequestHeader(
                        value = "X-INTERNAL-SECRET",
                        required = false
                    ) String internalSecret) {

                // Verify that the request comes from a trusted service
                if (internalSecret == null ||
                        !MessageDigest.isEqual(
                            serviceSecret.getBytes(StandardCharsets.UTF_8),
                            internalSecret.getBytes(StandardCharsets.UTF_8))) {

                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "Invalid service credentials"
                    );
                }

                service.reduceStock(id, quantity);

                return ResponseEntity.ok("Stock updated successfully");
            }

   

}
