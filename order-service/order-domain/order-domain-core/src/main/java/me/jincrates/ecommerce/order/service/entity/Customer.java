package me.jincrates.ecommerce.order.service.entity;

import me.jincrates.ecommerce.domain.entity.AggregateRoot;
import me.jincrates.ecommerce.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private String email;
    private String name;

    public Customer(CustomerId customerId, String email, String name) {
        super.setId(customerId);
        this.email = email;
        this.name = name;
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
