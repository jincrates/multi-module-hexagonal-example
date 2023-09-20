package me.jincrates.ecommerce.order.domain.valueobject;

import java.util.UUID;

public record StreetAddress(
    UUID id,
    String postalCode,
    String city,
    String street
) {

}
