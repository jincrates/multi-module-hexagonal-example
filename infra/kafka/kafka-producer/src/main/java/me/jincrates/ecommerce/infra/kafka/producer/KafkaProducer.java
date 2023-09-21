package me.jincrates.ecommerce.infra.kafka.producer;

import java.util.function.BiConsumer;
import org.springframework.kafka.support.SendResult;

public interface KafkaProducer {
    void send(String topicName, String key, Object message, BiConsumer<SendResult<String, Object>, Throwable> callback);
}
