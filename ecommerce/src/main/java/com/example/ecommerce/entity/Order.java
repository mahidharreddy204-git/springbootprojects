package com.example.ecommerce.entity;

import java.util.Date;
import java.util.List;

import com.example.ecommerce.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name ="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItem;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}