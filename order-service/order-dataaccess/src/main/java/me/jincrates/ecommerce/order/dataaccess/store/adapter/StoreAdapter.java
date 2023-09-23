package me.jincrates.ecommerce.order.dataaccess.store.adapter;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.domain.valueobject.StoreId;
import me.jincrates.ecommerce.order.dataaccess.store.entity.StoreEntity;
import me.jincrates.ecommerce.order.dataaccess.store.mapper.StoreDataAccessMapper;
import me.jincrates.ecommerce.order.dataaccess.store.repository.StoreJpaRepository;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.port.output.persistence.StorePort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreAdapter implements StorePort {
    private final StoreJpaRepository storeJpaRepository;
    private final StoreDataAccessMapper storeDataAccessMapper;

    @Override
    public Optional<Store> findStoreInformation(Store store) {
        List<UUID> storeProducts = storeDataAccessMapper.toStoreProducts(store);

        Optional<List<StoreEntity>> storeEntities = storeJpaRepository
                .findByStoreIdAndProductIdIn(store.getId().getValue(), storeProducts);

        return storeEntities.map(storeDataAccessMapper::toStore);
    }
}
