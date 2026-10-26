package com.example.food_service.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.food_service.dto.SubCategoryRequest;
import com.example.food_service.dto.SubCategoryResponse;
import com.example.food_service.entity.FoodCategory;
import com.example.food_service.entity.SubCategory;
import com.example.food_service.exceptionhandling.ResourceNotFoundException;
import com.example.food_service.repository.FoodCategoryRepository;
import com.example.food_service.repository.SubCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImplementation implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepo;
    private final FoodCategoryRepository foodCategoryRepo;

    @Override
    @CacheEvict (value="subCategories" ,allEntries = true)
    public SubCategoryResponse createSubCategory(SubCategoryRequest request) {

        FoodCategory category = foodCategoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Food category not found with "+request.getCategoryId()+"name"));

        SubCategory subCategory = new SubCategory();

        subCategory.setSubCategoryName(request.getSubCategoryName());

        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);

        return new SubCategoryResponse(
                saved.getId(),
                saved.getSubCategoryName(),
                saved.getCategory().getId()
        );
    }

    @Override
    @Cacheable (value="subCategories" , key="all")
    public List<SubCategoryResponse> getAllCategories() {

        return subCategoryRepo.findAll()
                .stream()
                .map(subCategory ->
                        new SubCategoryResponse(
                                subCategory.getId(),
                                subCategory.getSubCategoryName(),
                                subCategory.getCategory().getId()
                        )
                )
                .toList();
    }

    @Override
    @Cacheable (value="subCategories" , key="#id")
    public SubCategoryResponse getsubCategoryById(Long id) {

        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found with id : "+id));

        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getSubCategoryName(),
                subCategory.getCategory().getId()
        );
    }

    @Override
    @Caching (
        put={
                @CachePut (value="subCategories", key="#id")
        },
        evict={
                @CacheEvict (value="subCategories" , allEntries = true)
        }
    )
    public SubCategoryResponse updateSubCategory(
            Long id,
            SubCategoryRequest request) {

        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found with id :" +id));

        FoodCategory category = foodCategoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with :"+request.getSubCategoryName()));

        subCategory.setSubCategoryName(request.getSubCategoryName());

        subCategory.setCategory(category);

        SubCategory updated = subCategoryRepo.save(subCategory);

        return new SubCategoryResponse(
                updated.getId(),
                updated.getSubCategoryName(),
                updated.getCategory().getId()
        );
    }

    @Override
    @Caching (
        evict={
                @CacheEvict (value="subCategories" , key="#id"),
                @CacheEvict (value="subCategories", key="'all'")
        }
    )
    public void deleteSubCategory(Long id) {

        subCategoryRepo.deleteById(id);
    }
}