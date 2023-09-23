package me.jincrates.ecommerce.order.dataaccess.customer.adapter;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import me.jincrates.ecommerce.domain.valueobject.CustomerId;
import me.jincrates.ecommerce.order.dataaccess.customer.mapper.CustomerDataAccessMapper;
import me.jincrates.ecommerce.order.dataaccess.customer.repository.CustomerJpaRepository;
import me.jincrates.ecommerce.order.domain.entity.Customer;
import me.jincrates.ecommerce.order.port.output.persistence.CustomerPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerAdapter implements CustomerPort {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerDataAccessMapper::toCustomer);
    }

    @Override
    public Customer save(Customer customer) {
        return customerDataAccessMapper.toCustomer(
                customerJpaRepository.save(customerDataAccessMapper.toEntity(customer))
        );
    }
}
