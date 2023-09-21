package me.jincrates.ecommerce.order.service.response;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;

public record CreateOrderResponse(
    @NotNull
    UUID orderTrackingId,
    @NotNull
    OrderStatus orderStatus,
    @NotNull
    String message
) {

}
