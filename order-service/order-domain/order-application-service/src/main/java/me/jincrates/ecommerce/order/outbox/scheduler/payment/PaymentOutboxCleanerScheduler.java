package me.jincrates.ecommerce.order.outbox.scheduler.payment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.outbox.OutboxScheduler;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxSchedulerHelper paymentOutboxSchedulerHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        List<OrderPaymentOutboxMessage> outboxMessages = paymentOutboxSchedulerHelper
            .getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                OutboxStatus.COMPLETED,
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATED
            )
            .orElse(Collections.emptyList());

        if (!outboxMessages.isEmpty()) {
            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The payloads: {}",
                outboxMessages.size(),
                outboxMessages.stream()
                    .map(OrderPaymentOutboxMessage::getPayload)
                    .collect(Collectors.joining("\n")));
            deletePaymentOutboxMessages();
            log.info("{} OrderPaymentOutboxMessage deleted!", outboxMessages.size());
        }
    }

    private void deletePaymentOutboxMessages() {
        paymentOutboxSchedulerHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus.COMPLETED,
            SagaStatus.SUCCEEDED,
            SagaStatus.FAILED,
            SagaStatus.COMPENSATED
        );
    }
}
