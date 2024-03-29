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
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentEventPayload;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.PaymentOutboxPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxHelper {

    private final PaymentOutboxPort paymentOutboxPort;
    private final ObjectMapper objectMapper;

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload paymentEventPayload,
        OrderStatus orderStatus,
        SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {
        save(OrderPaymentOutboxMessage.builder()
            .id(UUID.randomUUID())
            .sagaId(sagaId)
            .createdAt(paymentEventPayload.createdAt())
            .type(ORDER_SAGA_NAME)
            .payload(createPayload(paymentEventPayload))
            .orderStatus(orderStatus)
            .sagaStatus(sagaStatus)
            .outboxStatus(outboxStatus)
            .build());
    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = paymentOutboxPort.save(orderPaymentOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}",
                orderPaymentOutboxMessage.getId());
            throw new OrderDomainException(
                "Could not save OrderPaymentOutboxMessage with outbox id: "
                    + orderPaymentOutboxMessage.getId());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}",
            orderPaymentOutboxMessage.getId());
    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(
        UUID sagaId, SagaStatus... sagaStatus) {
        return paymentOutboxPort.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId,
            sagaStatus);
    }

    private String createPayload(OrderPaymentEventPayload paymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(paymentEventPayload);
        } catch (JsonProcessingException ex) {
            log.error("Could not create OrderPaymentEventPayload object for order id: {}",
                paymentEventPayload.orderId(), ex);

            throw new OrderDomainException(
                "Could not create OrderPaymentEventPayload object for order id: " +
                    paymentEventPayload.orderId(), ex);
        }
    }
}
