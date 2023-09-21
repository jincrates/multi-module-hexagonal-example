package me.jincrates.ecommerce.order.port.output.persistence;

import java.util.Optional;
import me.jincrates.ecommerce.domain.valueobject.OrderId;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.valueobject.TrackingId;

public interface OrderPort {
    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
