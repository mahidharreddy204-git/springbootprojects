package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;

}
