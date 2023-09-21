package me.jincrates.ecommerce.order.service.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record TrackOrderQuery(
    @NotNull
    UUID orderTrackingId
) {

}
