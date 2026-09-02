package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.entity.Category;


public interface CategoryService {

     Category addCategory( CategoryRequest request);

    List<Category> getAllCategory();

    Category updateCategory(Long id ,CategoryRequest request);

    void deleteCategory(Long id);

    Category getCategory(Long id);

}
