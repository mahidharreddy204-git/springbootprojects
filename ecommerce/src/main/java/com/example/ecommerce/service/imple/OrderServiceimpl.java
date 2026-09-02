package com.example.ecommerce.service.imple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceimpl implements OrderService {

    private final OrderRepository orderRepo;

   // private final OrderItemsRepository orderItemsRepo;

    private final UserRepository userRepo;

    private final CartRepository cartRepo;

    private final CartItemRepository cartItemRepo;

    @Override
    @Transactional
    public Order placeOrder(){
        String username =
                        SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepo.findByUsername(username)
                            .orElseThrow(()->
                        new RuntimeException("user not found"));

        Cart cart = cartRepo.findByUser(user)
                        .orElseThrow(()->
                    new RuntimeException("Cart not found"));

        Order order = new Order();

        order.setUser(user);

        order.setOrderItem(new ArrayList<>());

        double total = 0;

        //convert the cart items into the order items
        for (CartItem cartItem : cart.getCartitem()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(cartItem.getProduct());

            orderItem.setQuantity(cartItem.getQuantity());

            double price =
                    cartItem.getProduct().getPrice()
                    * cartItem.getQuantity();

            orderItem.setPrice(price);

            total += price;

            order.getOrderItem().add(orderItem);
        }

        order.setTotalAmount(total);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepo.save(order);

        // Clear Cart
        cartItemRepo.deleteAll(cart.getCartitem());

        cart.getCartitem().clear();

        cartRepo.save(cart);
 
        return savedOrder;
    }

    @Override

    public List<Order> getMyOrder(){
        String username = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName();
        
        User user = userRepo.findByUsername(username)
                        .orElseThrow(()->
                    new RuntimeException("user not found"));
            
        return orderRepo.findByUser(user);
        
    }

    @Override
    public List<Order> getAllOrders(){
        return orderRepo.findAll();

    }
    }

    


