package me.jincrates.ecommerce.order.service.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import me.jincrates.ecommerce.domain.valueobject.PaymentStatus;

public record PaymentResponse(
    String id,
    String sagaId,
    String orderId,
    String paymentId,
    String customerId,
    BigDecimal price,
    Instant createdAt,
    PaymentStatus paymentStatus,
    List<String> failureMessages
) {

    public PaymentResponse {
        failureMessages = failureMessages == null ? new ArrayList<>() : failureMessages;
    }
}
