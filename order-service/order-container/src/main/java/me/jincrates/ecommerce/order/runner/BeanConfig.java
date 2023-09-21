package me.jincrates.ecommerce.order.runner;

import me.jincrates.ecommerce.order.service.OrderDomainService;
import me.jincrates.ecommerce.order.service.OrderDomainUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderDomainUseCase orderDomainUseCase() {
        return new OrderDomainService();
    }
}
