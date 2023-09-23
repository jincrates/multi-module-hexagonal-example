package me.jincrates.ecommerce.order.dataaccess.store.repository;

import me.jincrates.ecommerce.order.dataaccess.store.entity.StoreEntity;
import me.jincrates.ecommerce.order.dataaccess.store.entity.StoreEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreJpaRepository extends JpaRepository<StoreEntity, StoreEntityId> {

    Optional<List<StoreEntity>> findByStoreIdAndProductIdIn(UUID storeId, List<UUID> productIds);
}
