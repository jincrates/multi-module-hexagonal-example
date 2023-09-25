package me.jincrates.ecommerce.order.service.handler;

import static me.jincrates.ecommerce.domain.DomainConstants.UTC;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.domain.valueobject.OrderId;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;
import me.jincrates.ecommerce.domain.valueobject.PaymentStatus;
import me.jincrates.ecommerce.infra.outbox.OutboxStatus;
import me.jincrates.ecommerce.infra.saga.SagaStatus;
import me.jincrates.ecommerce.infra.saga.SagaStep;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.event.OrderPaidEvent;
import me.jincrates.ecommerce.order.domain.exception.OrderDomainException;
import me.jincrates.ecommerce.order.domain.exception.OrderNotFoundException;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalOutboxMessage;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentOutboxMessage;
import me.jincrates.ecommerce.order.port.output.persistence.OrderPort;
import me.jincrates.ecommerce.order.service.OrderDomainUseCase;
import me.jincrates.ecommerce.order.service.handler.helper.ApprovalOutboxHelper;
import me.jincrates.ecommerce.order.service.handler.helper.OrderSagaHelper;
import me.jincrates.ecommerce.order.service.handler.helper.PaymentOutboxHelper;
import me.jincrates.ecommerce.order.service.handler.mapper.OrderDataMapper;
import me.jincrates.ecommerce.order.service.response.PaymentResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSagaHandler implements SagaStep<PaymentResponse> {

    private final OrderDomainUseCase orderDomainUseCase;
    private final OrderPort orderPort;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderDataMapper orderDataMapper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
            UUID.fromString(paymentResponse.sagaId()),
            SagaStatus.STARTED
        );

        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", paymentResponse.sagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();
        OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);
        SagaStatus sagaStatus = orderSagaHelper.toSagaStatus(domainEvent.getOrder().getOrderStatus());

        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, domainEvent.getOrder().getOrderStatus(), sagaStatus)
        );

        approvalOutboxHelper.saveApprovalOutboxMessage(
            orderDataMapper.toOrderApprovalEventPayload(domainEvent),
            domainEvent.getOrder().getOrderStatus(),
            sagaStatus,
            OutboxStatus.STARTED,
            UUID.fromString((paymentResponse.sagaId()))
        );

        log.info("Order with id: {} is paid", domainEvent.getOrder().getId().getValue());
    }

    @Override
    public void rollback(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
            UUID.fromString(paymentResponse.sagaId()),
            SagaStatus.STARTED
        );

        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!", paymentResponse.sagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();
        Order order = rollbackPaymentForOrder(paymentResponse);
        SagaStatus sagaStatus = orderSagaHelper.toSagaStatus(order.getOrderStatus());

        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, order.getOrderStatus(), sagaStatus)
        );

        if (paymentResponse.paymentStatus() == PaymentStatus.CANCELLED) {
            approvalOutboxHelper.save(
                getUpdatedApprovalOutboxMessage(paymentResponse.sagaId(), order.getOrderStatus(), sagaStatus)
            );
        }
    }

    private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id: {}", paymentResponse.orderId());
        Order order = findOrder(paymentResponse.orderId());
        OrderPaidEvent domainEvent = orderDomainUseCase.payOrder(order);
        orderPort.save(order);

        return domainEvent;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
        OrderStatus orderStatus, SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId, OrderStatus orderStatus, SagaStatus sagaStatus) {
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
            approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.COMPENSATING);
        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in "
                + SagaStatus.COMPENSATING.name() + " status!");
        }
        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

    private Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderPort.findById(new OrderId(UUID.fromString(orderId)));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id: {}", paymentResponse.orderId());
        Order order = findOrder(paymentResponse.orderId());
        orderDomainUseCase.cancelOrder(order, paymentResponse.failureMessages());
        orderPort.save(order);
        return order;
    }
}
