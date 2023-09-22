package me.jincrates.ecommerce.order.dataaccess.customer.adapter;

import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.CustomerId;
import me.jincrates.ecommerce.order.domain.entity.Customer;
import me.jincrates.ecommerce.order.port.output.persistence.CustomerPort;
import org.springframework.stereotype.Component;

@Component
public class CustomerAdapter implements CustomerPort {

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return Optional.of(new Customer(
            new CustomerId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")),
            "email",
            "name"
        ));
    }

    @Override
    public Customer save(Customer customer) {
        return null;
    }
}
