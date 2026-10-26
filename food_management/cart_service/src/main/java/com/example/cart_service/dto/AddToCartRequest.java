package com.example.cart_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AddToCartRequest {
     @NotNull(message = "FoodItem id is required")
    private Long foodItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity should be greater than 0")
    private int quantity;

}
