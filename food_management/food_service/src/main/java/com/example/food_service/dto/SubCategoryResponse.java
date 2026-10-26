package com.example.food_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter 
@AllArgsConstructor 

public class SubCategoryResponse {

    private Long id;

    private String subCategoryName;

    
    private Long categoryId;

}
