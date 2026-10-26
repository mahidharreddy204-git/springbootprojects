package com.example.food_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class SubCategoryRequest {
    
    @NotBlank (message = "Sub category name is required")
    private String subCategoryName;

    @NotBlank(message = "category is required")
    private Long categoryId;


}
