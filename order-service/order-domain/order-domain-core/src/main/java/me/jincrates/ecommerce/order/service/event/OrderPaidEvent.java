package me.jincrates.ecommerce.order.service.event;

import java.time.ZonedDateTime;
import me.jincrates.ecommerce.order.service.entity.Order;

public class OrderPaidEvent extends OrderEvent {

    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
