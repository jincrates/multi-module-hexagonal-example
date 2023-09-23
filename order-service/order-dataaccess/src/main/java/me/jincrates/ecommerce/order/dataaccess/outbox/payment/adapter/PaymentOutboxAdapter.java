package me.jincrates.ecommerce.order.dataaccess.outbox.payment.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import me.jincrates.ecommerce.order.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import me.jincrates.ecommerce.order.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.PaymentOutboxPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOutboxAdapter implements PaymentOutboxPort {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxDataAccessMapper.toMessage(
                paymentOutboxJpaRepository.save(paymentOutboxDataAccessMapper.toEntity(orderPaymentOutboxMessage))
        );
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(
        String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return Optional.of(
                paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatus))
                        .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox could not be found for saga type " + type )
        )
        .stream()
        .map(paymentOutboxDataAccessMapper::toMessage)
        .toList());
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
        UUID sagaId, SagaStatus... sagaStatus) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
                .map(paymentOutboxDataAccessMapper::toMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
        SagaStatus... sagaStatus) {
            paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatus));
    }
}
