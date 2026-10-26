package com.example.food_service.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.food_service.dto.FoodCategoryRequest;
import com.example.food_service.dto.FoodCategoryResponse;
import com.example.food_service.entity.FoodCategory;
import com.example.food_service.exceptionhandling.ResourceNotFoundException;
import com.example.food_service.repository.FoodCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class FoodCategoryServiceImplementation  implements FoodCategoryService{

    private final FoodCategoryRepository foodCategoryrepo;

    @Override 
    @CacheEvict(value="category" , key="#id")
    public FoodCategoryResponse createCategory(FoodCategoryRequest request){

        FoodCategory  category = new FoodCategory();

        category.setCategoryName(request.getCategoryName());

        FoodCategory savedCategory = foodCategoryrepo.save(category);

        return new FoodCategoryResponse(savedCategory.getId(), savedCategory.getCategoryName());
    }

    @Override 
    @Cacheable(value="categories")
    public List<FoodCategoryResponse> getAllCategories(){
        return foodCategoryrepo.findAll()
                .stream()
                .map(category->new FoodCategoryResponse(category.getId(), category.getCategoryName()))
                .toList();
    }

    @Override 
    @Cacheable (value="category" , key="#id")
    public FoodCategoryResponse getCategoryById(Long id){
        FoodCategory category = foodCategoryrepo.findById(id)
                                    .orElseThrow(()->
                                new ResourceNotFoundException("Category not found"));

            return new FoodCategoryResponse(category.getId(), category.getCategoryName());
    }

    @Override 
    @Caching  (evict ={
        @CacheEvict (value="category", key="id"),
        @CacheEvict (value="categories", allEntries = true)
    })
    public FoodCategoryResponse updateCategory(Long id,FoodCategoryRequest request){
        FoodCategory category = foodCategoryrepo.findById(id)
                                    .orElseThrow(()->
                                new ResourceNotFoundException("category not found"));
                    
            category.setCategoryName(request.getCategoryName());

            FoodCategory saved = foodCategoryrepo.save(category);

            return new FoodCategoryResponse(saved.getId(), saved.getCategoryName());
    }

    @Override 
    @Caching(evict = {
    @CacheEvict(value = "category", key = "#id"),
    @CacheEvict(value = "categories", allEntries = true)
})
    public void deleteCategory(Long id){

        foodCategoryrepo.deleteById(id);

    }

}
