package com.example.food_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class FoodItemRequest {

    @NotBlank (message = "food item name is required")
    private String foodName;

    @NotNull (message = "price is required")
    @Min (value =1 ,message ="price should be greater than zero")
    private Double price;

    @NotNull (message = "Quantity is required")
    @Min (value = 0 , message = "Quantity should not be negative")
    private Integer quantity;

    @NotNull (message = "Expiry date is required")
    @Future (message = "Expiry date must be future")
    private LocalDate expiryDate;

    @NotNull (message = "subcategory is required")
    private Long subCategoryId;


}
