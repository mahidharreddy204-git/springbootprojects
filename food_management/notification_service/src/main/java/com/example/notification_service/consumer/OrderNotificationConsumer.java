
package com.example.notification_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.notification_service.dto.OrderPlacedEvent;

@Component
public class OrderNotificationConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderNotificationConsumer.class);

    public OrderNotificationConsumer() {
        log.info("OrderNotificationConsumer bean created");
    }

    @KafkaListener(topics = "order-placed"
    )
    public void consume(OrderPlacedEvent event) {
        log.info("Received order event: {}", event);

        System.out.println("======= ORDER NOTIFICATION =======");
        System.out.println("Order ID: " + event.orderId());
        System.out.println("User ID: " + event.userId());
        System.out.println("Total Amount: " + event.totalAmount());
        System.out.println("Status: " + event.status());
        System.out.println("Notification: Your order was placed successfully.");
        System.out.println("==================================");
    }
}
