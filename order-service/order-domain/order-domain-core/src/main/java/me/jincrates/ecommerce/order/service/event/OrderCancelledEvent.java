package me.jincrates.ecommerce.order.service.event;

import java.time.ZonedDateTime;
import me.jincrates.ecommerce.order.service.entity.Order;

public class OrderCancelledEvent extends OrderEvent {

    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
