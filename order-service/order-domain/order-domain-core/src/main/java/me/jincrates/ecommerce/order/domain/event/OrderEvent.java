package me.jincrates.ecommerce.order.domain.event;

import java.time.ZonedDateTime;
import me.jincrates.ecommerce.domain.event.DomainEvent;
import me.jincrates.ecommerce.order.domain.entity.Order;

public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
