package me.jincrates.ecommerce.order.domain.entity;

import me.jincrates.ecommerce.domain.entity.BaseEntity;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    private Product() {
    }

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
