package me.jincrates.ecommerce.order.service;

import java.util.List;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.domain.event.OrderCancelledEvent;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.domain.event.OrderPaidEvent;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Store store);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
