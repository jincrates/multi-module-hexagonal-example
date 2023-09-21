package me.jincrates.ecommerce.infra.kafka.producer;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.kafka.producer.exception.KafkaProducerException;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topicName, String key, Object message, BiConsumer<SendResult<String, Object>, Throwable> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            CompletableFuture<SendResult<String, Object>> kafkaResultFutre = kafkaTemplate.send(topicName, key, message);
            kafkaResultFutre.whenComplete(callback);
        } catch (KafkaException ex) {
            log.error("Error on kafka producer with key: {}, message: {}, and exception: {}", key, message,
                ex.getMessage());
            throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
