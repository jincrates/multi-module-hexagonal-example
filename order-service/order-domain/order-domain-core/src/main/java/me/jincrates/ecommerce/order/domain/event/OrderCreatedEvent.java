package me.jincrates.ecommerce.order.domain.event;

import java.time.ZonedDateTime;
import me.jincrates.ecommerce.order.domain.entity.Order;

public class OrderCreatedEvent extends OrderEvent {

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
