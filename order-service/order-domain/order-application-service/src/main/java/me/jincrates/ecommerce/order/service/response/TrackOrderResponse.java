package me.jincrates.ecommerce.order.service.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;

public record TrackOrderResponse(
    @NotNull
    UUID orderTrackingId,
    @NotNull
    OrderStatus orderStatus,
    List<String> failureMessages
) {

}
