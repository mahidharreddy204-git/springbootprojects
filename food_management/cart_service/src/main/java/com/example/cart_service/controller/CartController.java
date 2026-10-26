package com.example.cart_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.OrderResponse;
import com.example.cart_service.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping ("/cart")
@RequiredArgsConstructor 
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestParam  Long userId , @RequestBody AddToCartRequest request) {

        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @GetMapping("/get")
    public ResponseEntity<CartResponse> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(@RequestParam Long userId,@RequestParam Long foodItemId,@RequestParam int quantity){
        return ResponseEntity.ok(cartService.updateCart(userId, foodItemId, quantity));
    }

    @DeleteMapping ("/delete")
    public ResponseEntity<CartResponse> deleteCart(@RequestParam Long userId , @RequestParam Long foodItemId){
        return ResponseEntity.ok(cartService.removeCart(userId, foodItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
        @RequestParam Long userId) {

    cartService.clearCart(userId);

    return ResponseEntity.ok(
            "Cart cleared successfully");
}

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                cartService.checkoutCart(userId));
    }
}
