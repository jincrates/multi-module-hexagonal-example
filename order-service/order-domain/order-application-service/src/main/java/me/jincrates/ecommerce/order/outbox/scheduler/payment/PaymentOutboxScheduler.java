package me.jincrates.ecommerce.order.outbox.scheduler.payment;

import static me.jincrates.ecommerce.domain.DomainConstants.JOINING_MESSAGE_DELIMITER;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.outbox.OutboxScheduler;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.message.publisher.PaymentRequestMessagePublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler implements OutboxScheduler {

    private final PaymentOutboxSchedulerHelper paymentOutboxSchedulerHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
        initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        List<OrderPaymentOutboxMessage> outboxMessages = paymentOutboxSchedulerHelper
            .getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                OutboxStatus.STARTED,
                SagaStatus.STARTED,
                SagaStatus.COMPENSATING
            ).orElse(Collections.emptyList());

        if (!outboxMessages.isEmpty()) {
            log.info("Received {} OrderPaymentOutboxMessage ids: {}, sending to message bus!",
                outboxMessages.size(),
                outboxMessages.stream()
                    .map(outboxMessage -> outboxMessage.getId().toString())
                    .collect(Collectors.joining(JOINING_MESSAGE_DELIMITER))
            );
            outboxMessages.forEach(outboxMessage
                -> paymentRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            log.info("{} OrderPaymentOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
        OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        paymentOutboxSchedulerHelper.save(orderPaymentOutboxMessage);
        log.info("OrderPaymentOutboxMessage is updated with outbox status: {}",
            outboxStatus.name());
    }
}
