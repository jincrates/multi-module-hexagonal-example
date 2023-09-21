package me.jincrates.ecommerce.order.service.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import me.jincrates.ecommerce.order.domain.OrderAddress;
import me.jincrates.ecommerce.order.domain.entity.OrderItem;

public record CreateOrderCommand(
    @NotNull
    UUID customerId,
    @NotNull
    UUID storeId,
    @NotNull
    BigDecimal price,
    @NotNull
    List<OrderItem>items,
    @NotNull
    OrderAddress address
) {

}