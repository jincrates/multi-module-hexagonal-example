package me.jincrates.ecommerce.order.port.output.persistence;

import java.util.Optional;
import me.jincrates.ecommerce.order.domain.entity.Store;

public interface StorePort {
    Optional<Store> findStoreInformation(Store store);
}
