package me.jincrates.ecommerce.order.service.handler.helper;

import static me.jincrates.ecommerce.infra.saga.SagaConstants.ORDER_SAGA_NAME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.domain.exception.OrderDomainException;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalEventPayload;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.ApprovalOutboxPort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalOutboxHelper {

    private final ApprovalOutboxPort approvalOutboxPort;
    private final ObjectMapper objectMapper;

    public void saveApprovalOutboxMessage(OrderApprovalEventPayload orderApprovalEventPayload,
        OrderStatus orderStatus, SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {
        save(OrderApprovalOutboxMessage.builder()
            .id(UUID.randomUUID())
            .sagaId(sagaId)
            .createdAt(orderApprovalEventPayload.createdAt())
            .type(ORDER_SAGA_NAME)
            .payload(createPayload(orderApprovalEventPayload))
            .orderStatus(orderStatus)
            .sagaStatus(sagaStatus)
            .outboxStatus(outboxStatus)
            .build());
    }

    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(
        UUID sagaId, SagaStatus... sagaStatus) {
        return approvalOutboxPort.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId,
            sagaStatus);
    }

    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        OrderApprovalOutboxMessage response = approvalOutboxPort.save(orderApprovalOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderApprovalOutboxMessage with outbox id: {}",
                orderApprovalOutboxMessage.getId());
            throw new OrderDomainException(
                "Could not save OrderApprovalOutboxMessage with outbox id: " +
                    orderApprovalOutboxMessage.getId());
        }
        log.info("OrderApprovalOutboxMessage saved with outbox id: {}",
            orderApprovalOutboxMessage.getId());
    }

    private String createPayload(OrderApprovalEventPayload orderApprovalEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderApprovalEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderApprovalEventPayload for order id: {}",
                orderApprovalEventPayload.orderId(), e);
            throw new OrderDomainException(
                "Could not create OrderApprovalEventPayload for order id: " +
                    orderApprovalEventPayload.orderId(), e);
        }
    }
}
