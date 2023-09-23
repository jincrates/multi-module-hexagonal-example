package me.jincrates.ecommerce.order.dataaccess.outbox.payment.mapper;

import me.jincrates.ecommerce.order.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity toEntity(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage toMessage(PaymentOutboxEntity paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
