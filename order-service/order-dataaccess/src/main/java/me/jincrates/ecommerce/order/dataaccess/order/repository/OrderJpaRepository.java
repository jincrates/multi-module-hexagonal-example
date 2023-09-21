package me.jincrates.ecommerce.order.dataaccess.order.repository;

import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.order.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
