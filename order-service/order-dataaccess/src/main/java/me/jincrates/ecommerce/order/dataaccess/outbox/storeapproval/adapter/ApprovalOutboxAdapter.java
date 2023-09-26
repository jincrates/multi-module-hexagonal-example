package me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.exception.ApprovalOutboxNotFoundException;
import me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.mapper.ApprovalOutboxDataAccessMapper;
import me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.repository.ApprovalOutboxJpaRepository;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.ApprovalOutboxPort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalOutboxAdapter implements ApprovalOutboxPort {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxDataAccessMapper.toMessage(approvalOutboxJpaRepository.save(
            approvalOutboxDataAccessMapper.toEntity(orderApprovalOutboxMessage))
        );
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(
        String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return Optional.of(
            approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
                    Arrays.asList(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException(
                    "Approval outbox object could be found for saga type " + type))
                .stream()
                .map(approvalOutboxDataAccessMapper::toMessage)
                .toList()
        );
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
        UUID sagaId, SagaStatus... sagaStatus) {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId,
                Arrays.asList(sagaStatus))
            .map(approvalOutboxDataAccessMapper::toMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
        SagaStatus... sagaStatus) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
            Arrays.asList(sagaStatus));
    }
}
