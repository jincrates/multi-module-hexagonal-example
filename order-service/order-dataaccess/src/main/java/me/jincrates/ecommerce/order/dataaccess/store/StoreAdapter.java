package me.jincrates.ecommerce.order.dataaccess.store;

import java.util.Optional;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.port.output.persistence.StorePort;
import org.springframework.stereotype.Component;

@Component
public class StoreAdapter implements StorePort {

    @Override
    public Optional<Store> findStoreInformation(Store store) {
        return Optional.empty();
    }
}
