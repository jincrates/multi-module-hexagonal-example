package me.jincrates.ecommerce.order.messaging.publisher;

import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.kafka.producer.KafkaMessageHelper;
import me.jincrates.ecommerce.infra.kafka.producer.KafkaProducer;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.order.config.OrderServiceConfigData;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentEventPayload;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.message.publisher.PaymentRequestMessagePublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final KafkaProducer kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback
    ) {
        OrderPaymentEventPayload orderPaymentEventPayload = toEventPayLoad(
            orderPaymentOutboxMessage);
        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
            orderPaymentEventPayload.orderId(),
            sagaId);

        try {
            sendToKafka(orderPaymentOutboxMessage, outboxCallback, orderPaymentEventPayload,
                sagaId);
            log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                orderPaymentEventPayload.orderId(), sagaId);
        } catch (Exception ex) {
            log.error("Error while sending OrderPaymentEventPayload" +
                    " to kafka with order id: {} and saga id: {}, error: {}",
                orderPaymentEventPayload.orderId(), sagaId, ex.getMessage());
        }
    }

    private OrderPaymentEventPayload toEventPayLoad(
        OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
            orderPaymentOutboxMessage.getPayload(), OrderPaymentEventPayload.class);
    }

    private void sendToKafka(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback,
        OrderPaymentEventPayload orderPaymentEventPayload, String sagaId) {
        kafkaProducer.send(
            orderServiceConfigData.getPaymentRequestTopicName(),
            sagaId,
            orderPaymentEventPayload,
            kafkaMessageHelper.getKafkaCallback(
                orderServiceConfigData.getPaymentRequestTopicName(),
                orderPaymentEventPayload,
                orderPaymentOutboxMessage,
                outboxCallback,
                orderPaymentEventPayload.orderId(),
                "OrderPaymentEventPayload")
        );
    }
}
