package me.jincrates.ecommerce.order.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderPaymentEventPayload(
    @JsonProperty
    String orderId,
    @JsonProperty
    String customerId,
    @JsonProperty
    BigDecimal price,
    @JsonProperty
    ZonedDateTime createdAt,
    @JsonProperty
    String paymentOrderStatus
) {

}
