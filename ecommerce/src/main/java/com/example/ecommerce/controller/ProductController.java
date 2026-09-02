package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    
    private final ProductService productService;
      

    @PostMapping
     @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@Valid
        @RequestBody ProductRequest request) {
        return productService.addproduct(request);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }
    @GetMapping 
    public Page<Product> getAllProducts(
        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue ="5")
        int size
    ){
        return productService.getAllProducts(page, size);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct (@PathVariable Long id ,@RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}") 
    public String deleteProduct(@PathVariable Long id){
         productService.deleteProduct(id);
         return "product deleted sucessfully";
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductByCategoryId(@PathVariable Long categoryId) {
        return productService.getProductByCategoryId(categoryId);
    }
    
    
    
    

}
