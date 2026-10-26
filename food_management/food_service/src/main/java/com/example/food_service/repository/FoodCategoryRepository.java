package com.example.food_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.food_service.entity.FoodCategory;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

}
