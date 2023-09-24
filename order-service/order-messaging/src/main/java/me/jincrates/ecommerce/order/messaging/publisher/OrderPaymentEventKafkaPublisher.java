package me.jincrates.ecommerce.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.kafka.producer.KafkaMessageHelper;
import me.jincrates.ecommerce.infra.kafka.producer.KafkaProducer;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.order.config.OrderServiceConfigData;
import me.jincrates.ecommerce.order.messaging.mapper.OrderMessagingDataMapper;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentEventPayload;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.message.publisher.payment.PaymentRequestMessagePublisher;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback
    ) {
        OrderPaymentEventPayload orderPaymentEventPayload = kafkaMessageHelper.getEventPayload(orderPaymentOutboxMessage.getPayload(),
                OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

       log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.orderId(),
                sagaId);

       try {
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
           log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                   orderPaymentEventPayload.orderId(), sagaId);
       } catch (Exception ex) {
           log.error("Error while sending OrderPaymentEventPayload" +
                           " to kafka with order id: {} and saga id: {}, error: {}",
                   orderPaymentEventPayload.orderId(), sagaId, ex.getMessage());
       }
    }
}
