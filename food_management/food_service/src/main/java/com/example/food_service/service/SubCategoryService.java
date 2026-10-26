package com.example.food_service.service;

import java.util.List;

import com.example.food_service.dto.SubCategoryRequest;
import com.example.food_service.dto.SubCategoryResponse;

public interface SubCategoryService {

    SubCategoryResponse createSubCategory(SubCategoryRequest request);

    List<SubCategoryResponse> getAllCategories();

    SubCategoryResponse getsubCategoryById(Long id);

    SubCategoryResponse updateSubCategory(Long id,SubCategoryRequest request);

    void deleteSubCategory(Long id);
    

}
