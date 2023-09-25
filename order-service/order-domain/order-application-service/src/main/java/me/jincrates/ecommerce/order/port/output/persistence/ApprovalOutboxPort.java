package me.jincrates.ecommerce.order.port.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalOutboxMessage;

public interface ApprovalOutboxPort {
    OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage);

    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus);
    Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId, SagaStatus... sagaStatus);
    void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus);
}
