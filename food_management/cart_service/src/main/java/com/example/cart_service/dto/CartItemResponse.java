package com.example.cart_service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class CartItemResponse  implements Serializable{

    private static final long serialVersionUID=1L;

    private Long id;

    private Long foodItem;

    private int quantity;

}
