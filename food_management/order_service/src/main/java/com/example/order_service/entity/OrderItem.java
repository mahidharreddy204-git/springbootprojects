package com.example.order_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name="order_items")
@Data 
public class OrderItem {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long foodItemId;

    private int quantity;

    private double price;

    @ManyToOne 
    @JoinColumn (name="order_id")
    private Order order;
}
