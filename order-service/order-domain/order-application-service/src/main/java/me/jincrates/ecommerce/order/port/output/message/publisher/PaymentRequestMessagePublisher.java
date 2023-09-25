package me.jincrates.ecommerce.order.port.output.message.publisher;

import java.util.function.BiConsumer;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
