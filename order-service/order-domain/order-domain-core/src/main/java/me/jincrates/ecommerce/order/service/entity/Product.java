package me.jincrates.ecommerce.order.service.entity;

import me.jincrates.ecommerce.domain.entity.BaseEntity;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
