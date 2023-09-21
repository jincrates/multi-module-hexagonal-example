package me.jincrates.ecommerce.infra.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.kafka.producer.exception.KafkaProducerException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public <T> T getEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException ex) {
            log.error("Could not read {} object!", outputType.getName(), ex);
            throw new KafkaProducerException("Could not read " + outputType.getName() + " object!", ex);
        }
    }

//    public <T, U>BiConsumer<SendResult<String, T>, Throwable> getKafkaCallback(String responseTopicName, T model, U outboxMessage,
//        BiConsumer<U, OutboxStaus> outboxCallback, String orderId, String modelName) {
//
//    }
}
