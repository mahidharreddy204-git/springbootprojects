package com.example.order_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order_service.entity.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem,Long>{

}
