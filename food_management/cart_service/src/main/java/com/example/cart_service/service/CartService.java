package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.OrderResponse;

public interface CartService {

    CartResponse addToCart(Long userId,AddToCartRequest request);

    CartResponse getCart(Long userId);

    CartResponse updateCart(Long userId,Long foodItemId,int quantity);

    CartResponse removeCart(Long userId,Long foodItemId);

    void clearCart(Long userId);

    OrderResponse checkoutCart(Long userId);
}
