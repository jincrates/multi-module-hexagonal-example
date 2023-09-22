package me.jincrates.ecommerce.domain.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID> {
    private ProductId() {
        super(null);
    }

    public ProductId(UUID value) {
        super(value);
    }
}
