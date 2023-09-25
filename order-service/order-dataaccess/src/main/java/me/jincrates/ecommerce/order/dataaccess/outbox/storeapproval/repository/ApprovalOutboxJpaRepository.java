package me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.entity.ApprovalOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalOutboxJpaRepository extends JpaRepository<ApprovalOutboxEntity, UUID> {
    Optional<List<ApprovalOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);

    Optional<ApprovalOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId, List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);
}
