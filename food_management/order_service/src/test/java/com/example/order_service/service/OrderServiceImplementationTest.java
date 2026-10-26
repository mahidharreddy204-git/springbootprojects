
package com.example.order_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.order_service.clients.FoodServiceClients;
import com.example.order_service.Repository.OrderRepository;
import com.example.order_service.dto.FoodItemResponse;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.exceptionhandling.ResouceNotFoundException;
import com.example.order_service.producer.OrderEventProducer;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplementationTest {

    @Mock
    private OrderEventProducer orderEventProducer;

    @Mock
    private FoodServiceClients food_service;

    @Mock
    private OrderRepository orderRepo;

    @InjectMocks
    private OrderServiceImplementation orderService;

    private CircuitBreaker foodCircuitBreaker;
    private Retry foodServiceRetry;

    private Order order;
    private FoodItemResponse foodItem;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {

        foodCircuitBreaker = CircuitBreaker.ofDefaults("foodService");
        foodServiceRetry = Retry.ofDefaults("foodService");

        orderService = new OrderServiceImplementation(
                foodServiceRetry,
                orderEventProducer,
                food_service,
                orderRepo,
                foodCircuitBreaker
        );

        order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(400.0);
        order.setOrderItems(new ArrayList<>());

        foodItem = new FoodItemResponse(
                100L,
                "Pizza",
                200.0,
                10,
                LocalDate.now().plusDays(5),
                1L
        );

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setFoodItemId(100L);
        itemRequest.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(itemRequest));
    }

    // 1. Create order successfully
    @Test
    void createOrderTest() {

        when(food_service.getFoodItemById(100L))
                .thenReturn(foodItem);

        when(orderRepo.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        OrderResponse response =
                orderService.createOrder(10L, orderRequest);

        assertNotNull(response);

        verify(food_service).getFoodItemById(100L);

        verify(food_service).reduceStock(100L, 2);

        verify(orderRepo).save(any(Order.class));

        verify(orderEventProducer).publishOrderPlaced(any());
    }

    // 2. Get order successfully
    @Test
    void getOrderByIdTest() {

        order.setOrderItems(new ArrayList<>());

        when(orderRepo.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(1L);

        assertNotNull(response);

        verify(orderRepo).findById(1L);
    }

    // 3. Cancel order successfully
    @Test
    void cancelOrderTest() {

        order.setStatus(OrderStatus.PLACED);

        when(orderRepo.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepo.save(any(Order.class)))
                .thenReturn(order);

        orderService.cancelOrder(1L);

        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus()
        );

        verify(orderRepo).save(order);
    }

    // 4. Order not found
    @Test
    void getOrderNotFoundTest() {

        when(orderRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResouceNotFoundException.class,
                () -> orderService.getOrderById(1L)
        );

        verify(orderRepo).findById(1L);
    }
}
