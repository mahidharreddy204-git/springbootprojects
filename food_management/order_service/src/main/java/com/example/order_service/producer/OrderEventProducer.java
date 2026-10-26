
package com.example.order_service.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order_service.dto.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void publishOrderPlaced(OrderPlacedEvent event) {
        kafkaTemplate.send(
                "order-placed",
                event.orderId().toString(),
                event
        ).whenComplete((result, exception) -> {
    if (exception != null) {
        log.error("Failed to publish order event", exception);
    } else {
        log.info(
            "Kafka acknowledged topic={}, partition={}, offset={}",
            result.getRecordMetadata().topic(),
            result.getRecordMetadata().partition(),
            result.getRecordMetadata().offset()
        );
    }
});
    }
}
