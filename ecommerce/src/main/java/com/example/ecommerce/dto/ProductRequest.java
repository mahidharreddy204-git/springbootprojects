package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "price should be positive")
    private Double price;

    @Positive(message = "Stock should be positive")
    private Integer stock;

    @NotNull(message = "categoryId is required")
    private Long categoryId;

}
