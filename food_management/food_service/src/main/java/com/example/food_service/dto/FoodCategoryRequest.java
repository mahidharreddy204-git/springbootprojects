package com.example.food_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class FoodCategoryRequest {

    @NotBlank(message = "category Name is required")
    private String categoryName;

}
