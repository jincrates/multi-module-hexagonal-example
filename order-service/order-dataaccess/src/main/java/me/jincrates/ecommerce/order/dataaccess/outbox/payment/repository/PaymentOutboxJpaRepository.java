package me.jincrates.ecommerce.order.dataaccess.outbox.payment.repository;

import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    Optional<List<PaymentOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatuses);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId, List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);
}
