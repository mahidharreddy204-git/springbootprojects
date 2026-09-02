package com.example.ecommerce.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cart")
@Getter
@Setter
public class Cart{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @OneToOne
    @JsonIgnore
    @JoinColumn(name ="user_id")
    private User user;

    @OneToMany(
        mappedBy = "cart",
        cascade=CascadeType.ALL
    )

    private List<CartItem> cartitem;

}