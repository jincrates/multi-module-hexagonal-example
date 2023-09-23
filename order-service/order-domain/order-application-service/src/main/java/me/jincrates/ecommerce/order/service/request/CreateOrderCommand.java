package me.jincrates.ecommerce.order.service.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import me.jincrates.ecommerce.order.domain.OrderAddress;

public record CreateOrderCommand(
    @NotNull
    UUID customerId,
    @NotNull
    UUID storeId,
    @NotNull
    BigInteger price,
    @NotNull
    List<OrderItem> items,
    @NotNull
    OrderAddress address
) {
    public record OrderItem(
        @NotNull
         UUID productId,
        @NotNull
        Integer quantity,
        @NotNull
        BigInteger price,
        @NotNull
        BigInteger subTotal
    ) {

    }
}
