package com.example.cart_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.cart_service.clients.FoodServiceClient;
import com.example.cart_service.clients.OrderServiceClient;
import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartItemResponse;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.FoodItemResponse;
import com.example.cart_service.dto.OrderItemRequest;
import com.example.cart_service.dto.OrderRequest;
import com.example.cart_service.dto.OrderResponse;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.exceptionhandling.FoodExpiredException;
import com.example.cart_service.exceptionhandling.InsufficientStockException;
import com.example.cart_service.exceptionhandling.ResourceNotFoundException;
import com.example.cart_service.repository.CartItemRepository;
import com.example.cart_service.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodServiceClient food_service;
    private final OrderServiceClient orderServiceClient;

    
    @Override
    @CachePut  (value="Cart", key="#userId")
    public CartResponse addToCart(
            Long userId,
           AddToCartRequest  request) {

        FoodItemResponse foodItem = food_service.getFoodItemById(request.getFoodItemId());

        if(foodItem.getExpiryDate().isBefore(LocalDate.now())){
                throw new FoodExpiredException("Food is expired");
        }
        if(foodItem.getQuantity() < request.getQuantity()){
                throw new InsufficientStockException("Insufficient stock");
        }        

        // Find existing cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setCartItems(new ArrayList<>());

                    return cartRepository.save(newCart);
                });

        // Check whether food item already exists
        CartItem cartItem = cartItemRepository
                .findByCartIdAndFoodItemId(
                        cart.getId(),
                        request.getFoodItemId())
                .orElse(null);

        if (cartItem != null) {

            // Increase existing quantity
            cartItem.setQuantity(
                    cartItem.getQuantity()
                            + request.getQuantity());

        } else {

            // Create new cart item
            cartItem = new CartItem();

            cartItem.setFoodItemId(
                    request.getFoodItemId());

            cartItem.setQuantity(
                    request.getQuantity());

            cartItem.setCart(cart);

            cart.getCartItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        return mapToResponse(cart);
    }

    @Override
    @Cacheable (value="Cart", key="#userId")
    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        return mapToResponse(cart);
    }

    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items =
                cart.getCartItems()
                        .stream()
                        .map(item ->
                            new CartItemResponse(
                                item.getId(),
                                item.getFoodItemId(),
                                item.getQuantity()
                            ))
                        .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                items
        );
    }

    @Override 
    @CachePut(value = "Cart", key = "#userId")
    public CartResponse updateCart(Long userId,Long foodItemId,int quantity){

       if(quantity<0){
        throw new RuntimeException("Quantity should be greater than 0");
       }

       FoodItemResponse foodItem = food_service.getFoodItemById(foodItemId);

       if(quantity > foodItem.getQuantity()){
        throw new InsufficientStockException("insufficient quantity");
       }

       Cart cart = cartRepository.findByUserId(userId)
                        .orElseThrow(()->
                new ResourceNotFoundException("Cart not found"));
        
        CartItem cartItem = cartItemRepository.findByCartIdAndFoodItemId(cart.getId(), foodItemId)
                        .orElseThrow(()->
                new ResourceNotFoundException("FoodItem  not found in cart"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return mapToResponse(cart);
    }

    @Override 
    @CacheEvict (value="Cart",key="#userId")
    public CartResponse removeCart(Long userId,Long foodItemId){
        CartItem cartItem = cartItemRepository.findByCartIdAndFoodItemId(userId, foodItemId)
                        .orElseThrow(()->
                new ResourceNotFoundException("FoodItem not found"));
        
        Cart cart  = cartRepository.findByUserId(userId)
                        .orElseThrow(()->
                new ResourceNotFoundException("Cart not found"));
        
        cartItemRepository.delete(cartItem);

        return mapToResponse(cart);
    }
    @Override 
    @CacheEvict (value="Cart", key="#userId")
    public void clearCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                        .orElseThrow(()->
                new ResourceNotFoundException("Cart not found"));
        
         cartItemRepository.deleteAll(cart.getCartItems());

         cart.getCartItems().clear();
    }

    @Override 
    @CacheEvict(value = "Cart", key = "#userId")

    public OrderResponse checkoutCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->
                 new ResourceNotFoundException("Cart not found"));
        
        if(cart.getCartItems()==null || cart.getCartItems().isEmpty()){
                throw new ResourceNotFoundException("cart is empty");
        }
        
        List<OrderItemRequest> orderItems = cart.getCartItems()
                                                .stream()
                                                .map(cartItem ->{
                                                        OrderItemRequest item = new OrderItemRequest();

                                                item.setFoodItemId(cartItem.getFoodItemId());
                                                item.setQuantity(cartItem.getQuantity());
                                                return item;
                                                })
                                                .toList();
        
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(orderItems);

        OrderResponse orderResponse = orderServiceClient.createOrder(userId, orderRequest);

        cartItemRepository.deleteAll(cart.getCartItems());

        cart.getCartItems().clear();

        return orderResponse;
    }
}