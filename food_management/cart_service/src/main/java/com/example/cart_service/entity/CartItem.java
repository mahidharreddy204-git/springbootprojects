package com.example.cart_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "cart_items")
@Getter 
@Setter 
public class CartItem {

    @Id 
    @GeneratedValue  ( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long foodItemId;

    private int quantity;

    @ManyToOne 
    @JoinColumn (name= "cart_id")
    private Cart cart;

}
