package me.jincrates.ecommerce.order.port.output.message.publisher.payment;

import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
