package com.example.food_service.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.food_service.dto.FoodItemRequest;
import com.example.food_service.dto.FoodItemResponse;
import com.example.food_service.entity.FoodItem;
import com.example.food_service.entity.SubCategory;
import com.example.food_service.exceptionhandling.InsufficientStockException;
import com.example.food_service.exceptionhandling.ResourceNotFoundException;
import com.example.food_service.repository.FoodItemRepository;
import com.example.food_service.repository.SubCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodItemServiceImplementation implements FoodItemService {

    private final FoodItemRepository foodItemRepo;
    private final SubCategoryRepository subCategoryRepo;

    @Override
    @CacheEvict (value="items" ,allEntries = true)
    public FoodItemResponse createFoodItem(FoodItemRequest request) {

        SubCategory subCategory = subCategoryRepo.findById(request.getSubCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found"));

        FoodItem food = new FoodItem();

        food.setFoodName(request.getFoodName());
        food.setPrice(request.getPrice());
        food.setQuantity(request.getQuantity());
        food.setExpiryDate(request.getExpiryDate());
        food.setSubCategory(subCategory);

        FoodItem saved = foodItemRepo.save(food);

        return new FoodItemResponse(
                saved.getId(),
                saved.getFoodName(),
                saved.getPrice(),
                saved.getQuantity(),
                saved.getExpiryDate(),
                saved.getSubCategory().getId()
        );
    }

    @Override
    @CacheEvict  (value="foodItems" , key="'all'")
    public List<FoodItemResponse> getAllFoodItems() {

        return foodItemRepo.findAll()
                .stream()
                .map(foodItem ->
                        new FoodItemResponse(
                                foodItem.getId(),
                                foodItem.getFoodName(),
                                foodItem.getPrice(),
                                foodItem.getQuantity(),
                                foodItem.getExpiryDate(),
                                foodItem.getSubCategory().getId()
                        )
                )
                .toList();
    }

    @Override
    @Cacheable (value="foodItems",key="#id")
    public FoodItemResponse getFoodItemById(Long id) {

        FoodItem item = foodItemRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("FoodItem not found"));

        return new FoodItemResponse(
                item.getId(),
                item.getFoodName(),
                item.getPrice(),
                item.getQuantity(),
                item.getExpiryDate(),
                item.getSubCategory().getId()
        );
    }

    @Override
    @Caching(
    put = {
        @CachePut(value = "foodItems", key = "#id")
    },
    evict = {
        @CacheEvict(value = "foodItems", key = "'all'")
    }
)
    public FoodItemResponse updateFoodItem(
            Long id,
            FoodItemRequest request
    ) {

        FoodItem item = foodItemRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("FoodItem not found"));

        SubCategory subCategory = subCategoryRepo.findById(
                request.getSubCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("SubCategory not found"));

        item.setFoodName(request.getFoodName());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());
        item.setExpiryDate(request.getExpiryDate());
        item.setSubCategory(subCategory);

        FoodItem saved = foodItemRepo.save(item);

        return new FoodItemResponse(
                saved.getId(),
                saved.getFoodName(),
                saved.getPrice(),
                saved.getQuantity(),
                saved.getExpiryDate(),
                saved.getSubCategory().getId()
        );
    }

    @Override
    @Caching(evict = {
    @CacheEvict(value = "foodItems", key = "#id"),
    @CacheEvict(value = "foodItems", key = "'all'")
})
    public void deleteFoodItem(Long id) {

        foodItemRepo.deleteById(id);
    }

    @Override 
    @Caching(evict = {
    @CacheEvict(value = "foodItems", key = "#foodItemid"),
    @CacheEvict(value = "foodItems", key = "'all'")
})
    public void reduceStock(Long foodItemid,int quantity){

        FoodItem foodItem = foodItemRepo.findById(foodItemid)
                        .orElseThrow(()->
                 new ResourceNotFoundException("food item not found with id of"+foodItemid));
                
                 if(quantity<=0){
                        throw new IllegalArgumentException("qunatity should be positive");
                 }
                 if(foodItem.getQuantity()<quantity){
                        throw new InsufficientStockException("Insufficient stock of : "+foodItem.getFoodName());
                 }
                 foodItem.setQuantity(foodItem.getQuantity() - quantity);

                 foodItemRepo.save(foodItem); 

    }
}
