package me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.mapper;

import me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.entity.ApprovalOutboxEntity;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity toEntity(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
            .id(orderApprovalOutboxMessage.getId())
            .sagaId(orderApprovalOutboxMessage.getSagaId())
            .createdAt(orderApprovalOutboxMessage.getCreatedAt())
            .type(orderApprovalOutboxMessage.getType())
            .payload(orderApprovalOutboxMessage.getPayload())
            .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
            .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
            .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
            .version(orderApprovalOutboxMessage.getVersion())
            .build();
    }

    public OrderApprovalOutboxMessage toMessage(ApprovalOutboxEntity approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
            .id(approvalOutboxEntity.getId())
            .sagaId(approvalOutboxEntity.getSagaId())
            .createdAt(approvalOutboxEntity.getCreatedAt())
            .type(approvalOutboxEntity.getType())
            .payload(approvalOutboxEntity.getPayload())
            .orderStatus(approvalOutboxEntity.getOrderStatus())
            .sagaStatus(approvalOutboxEntity.getSagaStatus())
            .outboxStatus(approvalOutboxEntity.getOutboxStatus())
            .version(approvalOutboxEntity.getVersion())
            .build();
    }
}
