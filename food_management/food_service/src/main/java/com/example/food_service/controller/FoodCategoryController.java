package com.example.food_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food_service.dto.FoodCategoryRequest;
import com.example.food_service.dto.FoodCategoryResponse;
import com.example.food_service.service.FoodCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categoryName")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService service;

    @PostMapping("/add")
    public FoodCategoryResponse createFoodCategory(@RequestBody FoodCategoryRequest request){
        return service.createCategory(request);
    }

    @GetMapping("/get")
    public List<FoodCategoryResponse> getAllCategories(){
        return service.getAllCategories();
    }

    @GetMapping ("/get/{id}")
    public FoodCategoryResponse getCategoryById(@PathVariable Long id){
        return service.getCategoryById(id);
    }

    @PutMapping ("/update/{id}")
    public FoodCategoryResponse updateCategory(@PathVariable Long id ,@RequestBody FoodCategoryRequest request){
        return service.updateCategory(id, request);
    }

    @DeleteMapping("/del/{id}")
    public void deleteCategory(@PathVariable Long id){
         service.deleteCategory(id);
    }

}
