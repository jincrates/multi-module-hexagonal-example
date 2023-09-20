package me.jincrates.ecommerce.order.domain.valueobject;

import me.jincrates.ecommerce.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {

    public OrderItemId(Long value) {
        super(value);
    }
}
