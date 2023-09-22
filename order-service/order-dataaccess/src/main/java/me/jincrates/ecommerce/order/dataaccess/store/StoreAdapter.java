package me.jincrates.ecommerce.order.dataaccess.store;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.domain.valueobject.StoreId;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.port.output.persistence.StorePort;
import org.springframework.stereotype.Component;

@Component
public class StoreAdapter implements StorePort {

    @Override
    public Optional<Store> findStoreInformation(Store store) {

        return Optional.of(new Store(
            new StoreId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")),
            List.of(new Product(new ProductId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")), "상품명", new Money(new BigDecimal(1000)))),
            true
        ));
    }
}
