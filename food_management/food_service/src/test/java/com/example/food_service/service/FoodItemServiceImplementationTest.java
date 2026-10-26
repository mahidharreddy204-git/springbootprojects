
package com.example.food_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.food_service.dto.FoodItemRequest;
import com.example.food_service.dto.FoodItemResponse;
import com.example.food_service.entity.FoodItem;
import com.example.food_service.entity.SubCategory;
import com.example.food_service.exceptionhandling.InsufficientStockException;
import com.example.food_service.repository.FoodItemRepository;
import com.example.food_service.repository.SubCategoryRepository;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceImplementationTest {

    @Mock
    private FoodItemRepository foodItemRepo;

    @Mock
    private SubCategoryRepository subCategoryRepo;

    @InjectMocks
    private FoodItemServiceImplementation foodService;

    private FoodItem foodItem;
    private SubCategory subCategory;
    private FoodItemRequest request;

    @BeforeEach
    void setUp() {

        subCategory = new SubCategory();
        subCategory.setId(1L);

        foodItem = new FoodItem();
        foodItem.setId(100L);
        foodItem.setFoodName("Pizza");
        foodItem.setPrice(200.0);
        foodItem.setQuantity(10);
        foodItem.setExpiryDate(LocalDate.now().plusDays(5));
        foodItem.setSubCategory(subCategory);

        request = new FoodItemRequest();
        request.setFoodName("Pizza");
        request.setPrice(200.0);
        request.setQuantity(10);
        request.setExpiryDate(LocalDate.now().plusDays(5));
        request.setSubCategoryId(1L);
    }

    // 1. Create food item
    @Test
    void createFoodItemTest() {

        when(subCategoryRepo.findById(1L))
                .thenReturn(Optional.of(subCategory));

        when(foodItemRepo.save(any(FoodItem.class)))
                .thenReturn(foodItem);

        FoodItemResponse response =
                foodService.createFoodItem(request);

        assertNotNull(response);

        verify(subCategoryRepo).findById(1L);
        verify(foodItemRepo).save(any(FoodItem.class));
    }

    // 2. Get food item by ID
    @Test
    void getFoodItemByIdTest() {

        when(foodItemRepo.findById(100L))
                .thenReturn(Optional.of(foodItem));

        FoodItemResponse response =
                foodService.getFoodItemById(100L);

        assertNotNull(response);

        verify(foodItemRepo).findById(100L);
    }

    // 3. Update food item
    @Test
    void updateFoodItemTest() {

        when(foodItemRepo.findById(100L))
                .thenReturn(Optional.of(foodItem));

        when(subCategoryRepo.findById(1L))
                .thenReturn(Optional.of(subCategory));

        when(foodItemRepo.save(any(FoodItem.class)))
                .thenReturn(foodItem);

        FoodItemResponse response =
                foodService.updateFoodItem(100L, request);

        assertNotNull(response);

        verify(foodItemRepo).save(foodItem);
    }

    // 4. Reduce stock successfully
    @Test
    void reduceStockTest() {

        when(foodItemRepo.findById(100L))
                .thenReturn(Optional.of(foodItem));

        when(foodItemRepo.save(any(FoodItem.class)))
                .thenReturn(foodItem);

        foodService.reduceStock(100L, 3);

        assertEquals(7, foodItem.getQuantity());

        verify(foodItemRepo).save(foodItem);
    }

    // 5. Insufficient stock
    @Test
    void reduceStockInsufficientTest() {

        when(foodItemRepo.findById(100L))
                .thenReturn(Optional.of(foodItem));

        assertThrows(
                InsufficientStockException.class,
                () -> foodService.reduceStock(100L, 20)
        );

        verify(foodItemRepo, never()).save(any(FoodItem.class));
    }
}
