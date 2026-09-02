package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.entity.Cart;



public interface CartService {

     Cart addToCart( CartRequest request);

    Cart getCart();

    Cart updateCartItem(Long itemid, Integer quantity);

    void deleteCartItem(Long itemid);

}
