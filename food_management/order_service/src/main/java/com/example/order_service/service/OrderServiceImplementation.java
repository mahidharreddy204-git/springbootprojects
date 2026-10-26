
package com.example.order_service.service;

import com.example.order_service.clients.FoodServiceClients;
import com.example.order_service.Repository.OrderRepository;

import com.example.order_service.dto.FoodItemResponse;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderItemResponse;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.OrderPlacedEvent;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.entity.OrderStatus;

import com.example.order_service.exceptionhandling.FoodExpiredException;
import com.example.order_service.exceptionhandling.InSufficientStockException;
import com.example.order_service.exceptionhandling.OrderCancellationException;
import com.example.order_service.exceptionhandling.ResouceNotFoundException;

import com.example.order_service.producer.OrderEventProducer;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.FeignException;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final Retry foodServiceRetry;

    private final OrderEventProducer orderEventProducer;

    private final FoodServiceClients food_service;

    private final OrderRepository orderRepo;

    private final CircuitBreaker foodCircuitBreaker;


    // =========================================
    // CIRCUIT BREAKER - FOOD SERVICE CALLS
    // =========================================

        private <T> T callFoodService(Supplier<T> supplier) {

    Supplier<T> circuitBreakerSupplier =
            CircuitBreaker.decorateSupplier(
                    foodCircuitBreaker,
                    supplier
            );

    Supplier<T> retrySupplier =
            Retry.decorateSupplier(
                    foodServiceRetry,
                    circuitBreakerSupplier
            );

    return retrySupplier.get();
}

    private void callFoodService(Runnable operation) {

        try {

            foodCircuitBreaker.executeRunnable(operation);

        } catch (CallNotPermittedException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Food Service is unavailable. Circuit Breaker is OPEN."
            );

        } catch (FeignException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Food Service is unavailable.",
                    ex
            );
        }
    }


    // =========================================
    // CREATE ORDER
    // =========================================

    @Override
    @Transactional
    @CachePut(value = "Orders", key = "#result.id")
    public OrderResponse createOrder(
            Long userId,
            OrderRequest request) {

        // 1. Combine duplicate food items

        Map<Long, Integer> itemQuantities =
                request.getItems()
                        .stream()
                        .collect(Collectors.groupingBy(
                                OrderItemRequest::getFoodItemId,
                                Collectors.summingInt(
                                        OrderItemRequest::getQuantity
                                )
                        ));


        // 2. Create Order

        Order order = new Order();

        order.setUserId(userId);

        order.setOrderDate(LocalDateTime.now());

        order.setStatus(OrderStatus.PLACED);


        // 3. Create OrderItems

        List<OrderItem> orderItems =
                itemQuantities.entrySet()
                        .stream()
                        .map(entry -> {

                            Long foodItemId = entry.getKey();

                            int quantity = entry.getValue();


                            // 4. Get food details
                            // Protected by Circuit Breaker

                            FoodItemResponse foodItem =
                                    callFoodService(
                                            () -> food_service
                                                    .getFoodItemById(
                                                            foodItemId
                                                    )
                                    );


                            // 5. Check stock

                            if (quantity > foodItem.getQuantity()) {

                                throw new InSufficientStockException(
                                        "Insufficient stock for: "
                                                + foodItem.getFoodName()
                                );
                            }


                            // 6. Check expiry

                            if (foodItem.getExpiryDate()
                                    .isBefore(LocalDate.now())) {

                                throw new FoodExpiredException(
                                        "Food item has expired: "
                                                + foodItem.getFoodName()
                                );
                            }


                            // 7. Reduce stock
                            // Protected by Circuit Breaker

                            callFoodService(
                                    () -> food_service.reduceStock(
                                            foodItem.getId(),
                                            quantity
                                    )
                            );


                            // 8. Create OrderItem

                            OrderItem orderItem = new OrderItem();

                            orderItem.setFoodItemId(
                                    foodItem.getId()
                            );

                            orderItem.setQuantity(quantity);

                            orderItem.setPrice(
                                    foodItem.getPrice()
                            );

                            orderItem.setOrder(order);


                            return orderItem;

                        })
                        .toList();


        // 9. Set OrderItems

        order.setOrderItems(orderItems);


        // 10. Calculate total amount

        double totalAmount =
                orderItems.stream()
                        .mapToDouble(item ->
                                item.getPrice()
                                        * item.getQuantity()
                        )
                        .sum();

        order.setTotalAmount(totalAmount);


        // 11. Save Order

        Order savedOrder = orderRepo.save(order);


        // 12. Create Kafka Event

        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus().name()
        );


        // 13. Publish Kafka event after transaction commits

        if (TransactionSynchronizationManager
                .isSynchronizationActive()) {

            TransactionSynchronizationManager
                    .registerSynchronization(
                            new TransactionSynchronization() {

                                @Override
                                public void afterCommit() {

                                    orderEventProducer
                                            .publishOrderPlaced(event);
                                }
                            }
                    );

        } else {

            orderEventProducer.publishOrderPlaced(event);
        }


        // 14. Return Order Response

        return mapToResponse(savedOrder);

    }


    // =========================================
    // GET ORDER BY ID
    // =========================================

    @Override
    @Cacheable(value = "Orders", key = "#id")
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new ResouceNotFoundException(
                                "Order not found"
                        )
                );

        return mapToResponse(order);
    }


    // =========================================
    // CANCEL ORDER
    // =========================================

    @Override
    @Transactional
    @CacheEvict(value = "Orders", key = "#id")
    public void cancelOrder(Long id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new ResouceNotFoundException(
                                "Order Not Found"
                        )
                );


        // Cannot cancel delivered order

        if (order.getStatus() == OrderStatus.DELIVERED) {

            throw new OrderCancellationException(
                    "Order cannot be cancelled"
            );
        }


        // Cannot cancel already cancelled order

        if (order.getStatus() == OrderStatus.CANCELLED) {

            throw new OrderCancellationException(
                    "Order Already cancelled"
            );
        }


        // Update order status

        order.setStatus(OrderStatus.CANCELLED);


        // Save order

        orderRepo.save(order);

    }


    // =========================================
    // TEST FOOD SERVICE
    // =========================================

    @Override
    public FoodItemResponse testFoodService(Long id) {

        return callFoodService(
                () -> food_service.getFoodItemById(id)
        );

    }


    // =========================================
    // MAP ORDER TO RESPONSE
    // =========================================

    public OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items =
                order.getOrderItems()
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getId(),
                                        item.getFoodItemId(),
                                        item.getQuantity(),
                                        item.getPrice()
                                )
                        )
                        .toList();


        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );

    }

}
