package me.jincrates.ecommerce.order.domain.valueobject;

import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.BaseId;

public class TrackingId extends BaseId<UUID> {

    public TrackingId(UUID value) {
        super(value);
    }
}
