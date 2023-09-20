package me.jincrates.ecommerce.order.service.entity;

import java.util.List;
import me.jincrates.ecommerce.domain.entity.AggregateRoot;
import me.jincrates.ecommerce.domain.valueobject.StoreId;

public class Store extends AggregateRoot<StoreId> {
    private final List<Product> products;
    private boolean active;

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
