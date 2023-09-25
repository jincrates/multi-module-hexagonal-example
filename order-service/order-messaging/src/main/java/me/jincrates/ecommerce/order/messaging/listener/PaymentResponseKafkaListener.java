package me.jincrates.ecommerce.order.messaging.listener;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.domain.valueobject.PaymentStatus;
import me.jincrates.ecommerce.infra.kafka.consumer.KafkaConsumer;
import me.jincrates.ecommerce.order.domain.exception.OrderNotFoundException;
import me.jincrates.ecommerce.order.port.input.message.listener.PaymentResponseMessageListener;
import me.jincrates.ecommerce.order.service.response.PaymentResponse;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponse> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;

    @Override
    public void receive(
        @Payload List<PaymentResponse> messages,
        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
        @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        log.info(
            "{} number of payment responses received with keys:{}, partitions:{} and offsets: {}",
            messages.size(),
            keys.toString(),
            partitions.toString(),
            offsets.toString());

        messages.forEach(paymentResponse -> {
            try {
                processPaymentResponse(paymentResponse);
            } catch (OptimisticLockingFailureException e) {
                log.error(
                    "Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                    paymentResponse.orderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", paymentResponse.orderId());
            } catch (Exception e) {
                log.error("Error processing payment for order id: {}. Exception: {}",
                    paymentResponse.orderId(), e.getMessage());
            }
        });
    }

    private void processPaymentResponse(PaymentResponse paymentResponse) {
        PaymentStatus status = paymentResponse.paymentStatus();

        if (PaymentStatus.COMPLETED.equals(status)) {
            log.info("Processing successful payment for order id: {}",
                paymentResponse.orderId());
            paymentResponseMessageListener.paymentCompleted(paymentResponse);

        } else if (PaymentStatus.CANCELLED.equals(status)) {
            log.info("Processing cancelled payment for order id: {}",
                paymentResponse.orderId());
            paymentResponseMessageListener.paymentCancelled(paymentResponse);

        } else {
            log.warn("Received unknown payment status for order id: {}", paymentResponse.orderId());
        }
    }
}
