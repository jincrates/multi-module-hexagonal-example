package me.jincrates.ecommerce.order.domain.valueobject;

import me.jincrates.ecommerce.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {

    private OrderItemId() {
        super(null);
    }

    public OrderItemId(Long value) {
        super(value);
    }
}
