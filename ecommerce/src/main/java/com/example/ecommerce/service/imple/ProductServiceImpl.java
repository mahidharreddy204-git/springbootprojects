package com.example.ecommerce.service.imple;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productrepo;

    private final CategoryRepository categoryRepository;

    @Override
    public Product addproduct( ProductRequest request) {

    Category category = categoryRepository
            .findById(request.getCategoryId())
            .orElseThrow(() ->
                    new RuntimeException("Category not found")
            );

    Product product = new Product();

    product.setName(request.getName());

    product.setDescription(request.getDescription());

    product.setPrice(request.getPrice());

    product.setStock(request.getStock());

    product.setCategory(category);

    return productrepo.save(product);
}

        @Override
        public Page<Product> getAllProducts(int page,
                int size
        ){
                Pageable pageable = PageRequest.of(page, size);
                return productrepo.findAll(pageable);
        }
   @Override
   public void deleteProduct(Long id){
     Product product = productrepo.findById(id)
              .orElseThrow(()->
              new RuntimeException("product not found"));

               productrepo.delete(product);
   }

   @Override
   public Product getProduct(Long id){
         Product product = productrepo.findById(id)
              .orElseThrow(()->
              new RuntimeException("product not found"));
        return product;

   }
   @Override
public Product updateProduct(
        Long id,
        ProductRequest request
) {

    Product product = productrepo.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Product not found")
            );

    Category category = categoryRepository
            .findById(request.getCategoryId())
            .orElseThrow(() ->
                    new RuntimeException("Category not found")
            );

    product.setName(request.getName());

    product.setDescription(request.getDescription());

    product.setPrice(request.getPrice());

    product.setStock(request.getStock());

    product.setCategory(category);

    return productrepo.save(product);
}

        @Override
        public List<Product> getProductByCategoryId(Long categoryId){
                return productrepo.findByCategoryId(categoryId);
        }


} 
