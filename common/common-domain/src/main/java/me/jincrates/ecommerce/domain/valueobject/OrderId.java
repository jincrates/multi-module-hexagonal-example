package me.jincrates.ecommerce.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    private OrderId() {
        super(null);
    }

    public OrderId(UUID value) {
        super(value);
    }
}
