package me.jincrates.ecommerce.order.dataaccess.outbox;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.PaymentOutboxPort;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxAdapter implements PaymentOutboxPort {

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return null;
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(
        String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return Optional.empty();
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
        UUID sagaId, SagaStatus... sagaStatus) {
        return Optional.empty();
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
        SagaStatus... sagaStatus) {

    }
}
