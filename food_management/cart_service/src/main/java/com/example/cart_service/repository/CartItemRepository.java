package com.example.cart_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cart_service.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{

    Optional<CartItem> findByCartIdAndFoodItemId(Long id,Long foodItemId);

}
