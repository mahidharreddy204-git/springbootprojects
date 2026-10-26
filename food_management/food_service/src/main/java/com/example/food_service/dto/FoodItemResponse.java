    package com.example.food_service.dto;

    import java.io.Serializable;
import java.time.LocalDate;

    import lombok.AllArgsConstructor;
    import lombok.Getter;

    @Getter 
    @AllArgsConstructor 
    public class FoodItemResponse implements Serializable{

        private static final long serialVersionUID = 1L;

        private Long id;

        private String foodName;

        private Double price;

        private Integer quantity;

        private LocalDate expiryDate;

        private Long subCategoryId;

    }
