package com.example.cart_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data 
@AllArgsConstructor 
public class FoodItemResponse {

    private Long id;

    private String foodName;

    private Double price;

    private Integer quantity;

    private LocalDate expiryDate;

    private Long subCategoryId;

}
