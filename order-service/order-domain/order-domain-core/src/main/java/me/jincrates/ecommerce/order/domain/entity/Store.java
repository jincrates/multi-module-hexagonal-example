package me.jincrates.ecommerce.order.domain.entity;

import java.util.List;
import lombok.Builder;
import me.jincrates.ecommerce.domain.entity.AggregateRoot;
import me.jincrates.ecommerce.domain.valueobject.StoreId;

public class Store extends AggregateRoot<StoreId> {
    private final List<Product> products;
    private boolean active;

    @Builder
    public Store(StoreId storeId, List<Product> products, boolean active) {
        super.setId(storeId);
        this.products = products;
        this.active = active;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }
}
