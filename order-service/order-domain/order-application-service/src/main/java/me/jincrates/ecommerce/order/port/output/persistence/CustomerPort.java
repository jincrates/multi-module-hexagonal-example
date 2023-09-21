package me.jincrates.ecommerce.order.port.output.persistence;

import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.order.domain.entity.Customer;

public interface CustomerPort {
    Optional<Customer> findCustomer(UUID customerId);

    Customer save(Customer customer);
}
