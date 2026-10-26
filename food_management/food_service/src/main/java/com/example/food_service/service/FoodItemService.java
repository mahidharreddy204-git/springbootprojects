package com.example.food_service.service;

import java.util.List;

import com.example.food_service.dto.FoodItemRequest;
import com.example.food_service.dto.FoodItemResponse;

public interface FoodItemService {

    FoodItemResponse createFoodItem(FoodItemRequest request);

    List<FoodItemResponse> getAllFoodItems();

    FoodItemResponse getFoodItemById(Long id);

    FoodItemResponse updateFoodItem(Long id, FoodItemRequest request);

    void deleteFoodItem(Long id);

    void reduceStock(Long foodItemid, int quantity);


    
}
