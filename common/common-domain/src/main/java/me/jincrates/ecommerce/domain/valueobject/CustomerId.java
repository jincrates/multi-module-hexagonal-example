package me.jincrates.ecommerce.domain.valueobject;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {

    private CustomerId() {
        super(null);
    }

    public CustomerId(UUID value) {
        super(value);
    }
}
