package com.example.food_service.service;

import java.util.List;

import com.example.food_service.dto.FoodCategoryRequest;
import com.example.food_service.dto.FoodCategoryResponse;

public interface FoodCategoryService {

    FoodCategoryResponse createCategory(FoodCategoryRequest request);

    List<FoodCategoryResponse> getAllCategories();

    FoodCategoryResponse getCategoryById(Long id);

    FoodCategoryResponse updateCategory(Long id,FoodCategoryRequest request);

    void deleteCategory(Long id);
}
