package me.jincrates.ecommerce.order.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.time.ZonedDateTime;

public record OrderPaymentEventPayload(
    @JsonProperty
    String orderId,
    @JsonProperty
    String customerId,
    @JsonProperty
    BigInteger price,
    @JsonProperty
    ZonedDateTime createdAt,
    @JsonProperty
    String paymentOrderStatus
) {

}
