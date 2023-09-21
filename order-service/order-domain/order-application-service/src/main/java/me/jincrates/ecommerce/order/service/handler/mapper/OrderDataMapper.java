package me.jincrates.ecommerce.order.service.handler.mapper;

import me.jincrates.ecommerce.domain.valueobject.PaymentOrderStatus;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentEventPayload;
import me.jincrates.ecommerce.order.service.response.CreateOrderResponse;
import me.jincrates.ecommerce.order.service.response.TrackOrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDataMapper {

    public CreateOrderResponse toCreateOrderResponse(Order order, String message) {
        return new CreateOrderResponse(
            order.getTrackingId().getValue(),
            order.getOrderStatus(),
            message
        );
    }

    public OrderPaymentEventPayload toOrderPaymentEventPayload(
        OrderCreatedEvent orderCreatedEvent) {
        return new OrderPaymentEventPayload(
            orderCreatedEvent.getOrder().getCustomerId().getValue().toString(),
            orderCreatedEvent.getOrder().getId().getValue().toString(),
            orderCreatedEvent.getOrder().getPrice().getAmount(),
            orderCreatedEvent.getCreatedAt(),
            PaymentOrderStatus.PENDING.name()
        );
    }

    public TrackOrderResponse toTrackOrderResponse(Order order) {
        return new TrackOrderResponse(
            order.getTrackingId().getValue(),
            order.getOrderStatus(),
            order.getFailureMessages()
        );
    }
}
