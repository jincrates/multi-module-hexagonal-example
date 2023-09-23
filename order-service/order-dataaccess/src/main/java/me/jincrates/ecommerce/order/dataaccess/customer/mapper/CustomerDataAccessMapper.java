package me.jincrates.ecommerce.order.dataaccess.customer.mapper;

import me.jincrates.ecommerce.domain.valueobject.CustomerId;
import me.jincrates.ecommerce.order.dataaccess.customer.entity.CustomerEntity;
import me.jincrates.ecommerce.order.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer toCustomer(CustomerEntity customerEntity) {
        return new Customer(
                new CustomerId(customerEntity.getId()),
                customerEntity.getEmail(),
                customerEntity.getName());
    }

    public CustomerEntity toEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .email(customer.getEmail())
                .name(customer.getName())
                .build();
    }
}
