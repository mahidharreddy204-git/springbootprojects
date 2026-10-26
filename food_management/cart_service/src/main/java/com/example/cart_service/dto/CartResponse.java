package com.example.cart_service.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter 
@AllArgsConstructor 
public class CartResponse implements Serializable{

    private static final long serialVersionUID=1L;

    private Long id;

    private Long userId;

    private List<CartItemResponse> items;



}
