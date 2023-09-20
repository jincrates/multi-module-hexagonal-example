package me.jincrates.ecommerce.domain.valueobject;

import java.util.UUID;

public class StoreId extends BaseId<UUID> {

    public StoreId(UUID value) {
        super(value);
    }
}
