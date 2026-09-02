package com.example.ecommerce.service.imple;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{


    private final CategoryRepository categoryRepo;
    
    @Override
    public Category addCategory(CategoryRequest request){

        Category category = new Category();
            category.setName(request.getName());
            return categoryRepo.save(category);
    }

    @Override
    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(Long id,CategoryRequest request){
        Category category = categoryRepo.findById(id)
                        .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );
                category.setName(request.getName());
               return categoryRepo.save(category);

                }

                @Override
                public void deleteCategory(Long id){
                    Category category = categoryRepo.findById(id)
                            .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );
                    categoryRepo.delete(category);

                }

                @Override
                public Category getCategory(Long id){
                     Category category = categoryRepo.findById(id)
                     .orElseThrow(()-> new RuntimeException("Category not found"));

                     return category;
                }


}
