package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(@Valid
                @RequestBody CartRequest request ) {

        return cartService.addToCart(request);
    }
    
    @GetMapping()
    public Cart getCart() {
        return cartService.getCart();
    } 
 
    @PostMapping("/update/{itemId}")
    public Cart updateCartItem(@PathVariable Long itemId, @RequestParam Integer quantity) {

        return cartService.updateCartItem(itemId, quantity);
    }

    @DeleteMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId){

        cartService.deleteCartItem(itemId);

        return "Item deleted from cart";

    }  
    

}
