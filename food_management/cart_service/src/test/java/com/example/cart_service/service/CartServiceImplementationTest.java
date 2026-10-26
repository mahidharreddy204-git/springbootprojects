
package com.example.cart_service.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.cart_service.clients.FoodServiceClient;
import com.example.cart_service.clients.OrderServiceClient;
import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.FoodItemResponse;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.repository.CartItemRepository;
import com.example.cart_service.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private FoodServiceClient food_service;

    @Mock
    private OrderServiceClient orderServiceClient;

    @InjectMocks
    private CartServiceImplementation cartService;

    private Cart cart;
    private CartItem cartItem;
    private FoodItemResponse foodItem;

    @BeforeEach
    void setUp() {

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(10L);
        cart.setCartItems(new ArrayList<>());

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setFoodItemId(100L);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);

         foodItem = new FoodItemResponse(
                100L,
                "Pizza",
                200.0,
                10,
                LocalDate.now().plusDays(5),
                1L
        );
    }

    // 1. Add item to cart successfully
    @Test
    void addToCartTest() {

        AddToCartRequest request = new AddToCartRequest();
        request.setFoodItemId(100L);
        request.setQuantity(2);

        when(food_service.getFoodItemById(100L))
                .thenReturn(foodItem);

        when(cartRepository.findByUserId(10L))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndFoodItemId(1L, 100L))
                .thenReturn(Optional.empty());

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse response = cartService.addToCart(10L, request);

        assertNotNull(response);

        verify(food_service).getFoodItemById(100L);

        verify(cartItemRepository).save(any(CartItem.class));
    }

    // 2. Get cart successfully
    @Test
    void getCartTest() {

        cart.getCartItems().add(cartItem);

        when(cartRepository.findByUserId(10L))
                .thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCart(10L);

        assertNotNull(response);

        verify(cartRepository).findByUserId(10L);
    }

    // 3. Update cart item quantity
    @Test
    void updateCartTest() {

        when(food_service.getFoodItemById(100L))
                .thenReturn(foodItem);

        when(cartRepository.findByUserId(10L))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndFoodItemId(1L, 100L))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse response = cartService.updateCart(
                10L,
                100L,
                5
        );

        assertNotNull(response);

        assertEquals(5, cartItem.getQuantity());

        verify(cartItemRepository).save(cartItem);
    }

    // 4. Remove item from cart
    @Test
    void removeCartTest() {

        cart.getCartItems().add(cartItem);

        when(cartItemRepository.findByCartIdAndFoodItemId(10L, 100L))
                .thenReturn(Optional.of(cartItem));

        when(cartRepository.findByUserId(10L))
                .thenReturn(Optional.of(cart));

        CartResponse response = cartService.removeCart(10L, 100L);

        assertNotNull(response);

        verify(cartItemRepository).delete(cartItem);
    }
}
