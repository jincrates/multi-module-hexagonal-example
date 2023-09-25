package me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;

@Getter
@Entity
@Table(name = "store_approval_outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalOutboxEntity {
    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;

    @Builder
    private ApprovalOutboxEntity(UUID id, UUID sagaId, ZonedDateTime createdAt,
        ZonedDateTime processedAt, String type, String payload, SagaStatus sagaStatus,
        OrderStatus orderStatus, OutboxStatus outboxStatus, int version) {
        this.id = id;
        this.sagaId = sagaId;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.type = type;
        this.payload = payload;
        this.sagaStatus = sagaStatus;
        this.orderStatus = orderStatus;
        this.outboxStatus = outboxStatus;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApprovalOutboxEntity that = (ApprovalOutboxEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
