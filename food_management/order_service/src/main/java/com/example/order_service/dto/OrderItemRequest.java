package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class OrderItemRequest {

    @NotNull (message = "Food id is required")
    private Long foodItemId;

    @NotNull (message = "Qunatity is required")
    @Min(value=1,message="Qunatity must be atleast 1")
    private Integer quantity;


}
