package me.jincrates.ecommerce.order.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"me.jincrates.ecommerce.order.dataaccess"})
@EntityScan(basePackages = {"me.jincrates.ecommerce.order.dataaccess"})
@SpringBootApplication(scanBasePackages = "me.jincrates.ecommerce")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
