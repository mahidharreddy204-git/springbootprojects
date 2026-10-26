package com.example.food_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.food_service.entity.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {

}
