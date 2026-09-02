package com.example.ecommerce.service.imple;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceimpl implements CartService{

    private final CartRepository cartRepo;

    private final UserRepository userRepo;

    private final ProductRepository productRepo;

    private final CartItemRepository cartItemRepo;

    @Override
    public Cart addToCart(CartRequest request){

        //LOGGED-IN USER
        String username = 
                SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        //FIND USER
        User user = userRepo.findByUsername(username)
                        .orElseThrow(()->
                        new RuntimeException("User not found"));

        //FIND PRODUCT 
        Product product = productRepo.findById(request.getProductId())
                            .orElseThrow(()->
                        new RuntimeException("Product not found")) ;
        
        //FIND CART

        Cart cart = cartRepo.findByUser(user)
                    .orElse(null);
        
        //CREATE CART IF NOT EXISTS

        if(cart == null ){
             cart = new Cart();
            cart.setUser(user);
            cart.setCartitem(new ArrayList<>());
            cart = cartRepo.save(cart);
        }

        // Create CartItem

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);

        cartItem.setProduct(product);

        cartItem.setQuantity(request.getQuantity());

        cartItemRepo.save(cartItem);

        cart.getCartitem().add(cartItem);

        return cartRepo.save(cart);
        
    }

    @Override
    public Cart getCart(){
        String username = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName() ;

        User user = userRepo.findByUsername(username)
                        .orElseThrow(()->
                        new RuntimeException("User not found"));

        return cartRepo.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );
    }

    @Override
    public Cart updateCartItem(
        Long itemId,
        Integer quantity
    ){
        CartItem cartItem = cartItemRepo
                .findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart Item not found")
                );

        cartItem.setQuantity(quantity);

        cartItemRepo.save(cartItem);

        return cartItem.getCart();
    }

    @Override
    public void deleteCartItem(Long itemId){
         CartItem cartItem = cartItemRepo
                .findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart Item not found")
                );

        cartItemRepo.delete(cartItem);
    }


}
