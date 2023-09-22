package me.jincrates.ecommerce.order.dataaccess.customer.adapter;

import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.order.domain.entity.Customer;
import me.jincrates.ecommerce.order.port.output.persistence.CustomerPort;
import org.springframework.stereotype.Component;

@Component
public class CustomerAdapter implements CustomerPort {

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return Optional.empty();
    }

    @Override
    public Customer save(Customer customer) {
        return null;
    }
}
