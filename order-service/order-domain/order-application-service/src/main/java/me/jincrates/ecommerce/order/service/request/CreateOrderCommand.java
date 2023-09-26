package me.jincrates.ecommerce.order.service.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

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

    public record OrderAddress(
        @NotNull
        @Max(value = 10)
        String postalCode,
        @NotNull
        @Max(value = 50)
        String city,
        @NotNull
        @Max(value = 50)
        String street
    ) {

    }
}
