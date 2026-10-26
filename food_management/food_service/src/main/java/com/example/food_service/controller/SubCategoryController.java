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

import com.example.food_service.dto.SubCategoryRequest;
import com.example.food_service.dto.SubCategoryResponse;
import com.example.food_service.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/sub")
@RequiredArgsConstructor 
public class SubCategoryController {
    private final SubCategoryService service;

    @PostMapping("/add")
    public SubCategoryResponse createSubCategory(@RequestBody SubCategoryRequest request){
        return service.createSubCategory(request);
    }

    @GetMapping("/get")
    public List<SubCategoryResponse> getAllCategories(){
        return service.getAllCategories();
    }

    @GetMapping ("/get/{id}")
    public SubCategoryResponse getCategoryById(@PathVariable  Long id){
        return service.getsubCategoryById(id);
    }

    @PutMapping ("/update/{id}")
    public SubCategoryResponse updateSubCategory(@PathVariable Long id , @RequestBody SubCategoryRequest request){
            return service.updateSubCategory(id, request);
    }

    @DeleteMapping ("/del/{id}")
    public void deleteSubCategory(@PathVariable Long id){
        service.deleteSubCategory(id);
    }
}
