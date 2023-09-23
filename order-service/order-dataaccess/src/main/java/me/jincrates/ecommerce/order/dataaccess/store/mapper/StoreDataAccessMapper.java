package me.jincrates.ecommerce.order.dataaccess.store.mapper;

import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.domain.valueobject.StoreId;
import me.jincrates.ecommerce.order.dataaccess.store.entity.StoreEntity;
import me.jincrates.ecommerce.order.dataaccess.store.exception.StoreDataAccessException;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class StoreDataAccessMapper {

    public List<UUID> toStoreProducts(Store store) {
        return store.getProducts().stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Store toStore(List<StoreEntity> storeEntities) {
        StoreEntity storeEntity = storeEntities.stream().findFirst()
                .orElseThrow(() -> new StoreDataAccessException("Store could not be found!"));

        List<Product> storeProducts = storeEntities.stream()
                .map(entity -> new Product(
                        new ProductId(entity.getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice())
                ))
                .toList();

        return Store.builder()
                .storeId(new StoreId(storeEntity.getStoreId()))
                .products(storeProducts)
                .active(storeEntity.getStoreActive())
                .build();
    }
}
