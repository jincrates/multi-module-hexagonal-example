package me.jincrates.ecommerce.order.service.handler.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalEventPayload;
import me.jincrates.ecommerce.order.port.output.persistence.ApprovalOutboxPort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalOutboxHelper {

    private final ApprovalOutboxPort approvalOutboxPort;
    private final ObjectMapper objectMapper;

    public void saveApprovalOutboxMessage(OrderApprovalEventPayload orderApprovalEventPayload,
        SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID uuid) {
    }
}
