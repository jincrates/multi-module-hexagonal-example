package me.jincrates.ecommerce.order.service.handler;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.service.handler.helper.OrderCreateHelper;
import me.jincrates.ecommerce.order.service.handler.helper.OrderSagaHelper;
import me.jincrates.ecommerce.order.service.handler.helper.PaymentOutboxHelper;
import me.jincrates.ecommerce.order.service.handler.mapper.OrderDataMapper;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import me.jincrates.ecommerce.order.service.response.CreateOrderResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());

        CreateOrderResponse createOrderResponse = orderDataMapper.toCreateOrderResponse(
            orderCreatedEvent.getOrder(),
            "Order created successfully");

        savePaymentOutboxMessage(orderCreatedEvent);

        log.info("Returning CreateOrderResponse with order id: {}",
            orderCreatedEvent.getOrder().getId());

        return createOrderResponse;
    }

    private void savePaymentOutboxMessage(OrderCreatedEvent orderCreatedEvent) {
        paymentOutboxHelper.savePaymentOutboxMessage(
            orderDataMapper.toOrderPaymentEventPayload(orderCreatedEvent),
            orderCreatedEvent.getOrder().getOrderStatus(),
            orderSagaHelper.toSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
            OutboxStatus.STARTED,
            UUID.randomUUID());
    }
}
