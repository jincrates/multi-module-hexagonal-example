package me.jincrates.ecommerce.order.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderApprovalEventPayload(
    @JsonProperty
    String orderId,
    @JsonProperty
    String storeId,
    @JsonProperty
    String storeOrderStatus,
    @JsonProperty
    BigInteger price,
    @JsonProperty
    ZonedDateTime createdAt,
    @JsonProperty
    List<OrderApprovalEventProduct> products
) {
    public record OrderApprovalEventProduct(
        @JsonProperty
        String id,
        @JsonProperty
        Integer quantity
    ) {

    }
}
