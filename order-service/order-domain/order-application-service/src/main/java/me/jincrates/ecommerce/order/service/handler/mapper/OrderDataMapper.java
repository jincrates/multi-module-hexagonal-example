package me.jincrates.ecommerce.order.service.handler.mapper;

import java.util.List;
import java.util.UUID;
import me.jincrates.ecommerce.domain.valueobject.CustomerId;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.PaymentOrderStatus;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.domain.valueobject.StoreId;
import me.jincrates.ecommerce.domain.valueobject.StoreOrderStatus;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.entity.OrderItem;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.domain.event.OrderPaidEvent;
import me.jincrates.ecommerce.order.domain.valueobject.StreetAddress;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalEventPayload;
import me.jincrates.ecommerce.order.outbox.model.OrderApprovalEventPayload.OrderApprovalEventProduct;
import me.jincrates.ecommerce.order.outbox.model.OrderPaymentEventPayload;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand.OrderAddress;
import me.jincrates.ecommerce.order.service.response.CreateOrderResponse;
import me.jincrates.ecommerce.order.service.response.TrackOrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDataMapper {

    public CreateOrderResponse toCreateOrderResponse(Order order, String message) {
        return new CreateOrderResponse(
            order.getTrackingId().getValue(),
            order.getOrderStatus(),
            message
        );
    }

    public OrderPaymentEventPayload toOrderPaymentEventPayload(
        OrderCreatedEvent orderCreatedEvent) {
        return new OrderPaymentEventPayload(
            orderCreatedEvent.getOrder().getCustomerId().getValue().toString(),
            orderCreatedEvent.getOrder().getId().getValue().toString(),
            orderCreatedEvent.getOrder().getPrice().getAmount(),
            orderCreatedEvent.getCreatedAt(),
            PaymentOrderStatus.PENDING.name()
        );
    }

    public TrackOrderResponse toTrackOrderResponse(Order order) {
        return new TrackOrderResponse(
            order.getTrackingId().getValue(),
            order.getOrderStatus(),
            order.getFailureMessages()
        );
    }

    public Order toOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
            .customerId(new CustomerId(createOrderCommand.customerId()))
            .storeId(new StoreId(createOrderCommand.storeId()))
            .deliveryAddress(toDeliveryAddress(createOrderCommand.address()))
            .price(new Money(createOrderCommand.price()))
            .items(toOrderItemEntities(createOrderCommand.items()))
            .build();
    }

    private StreetAddress toDeliveryAddress(OrderAddress orderAddress) {
        return new StreetAddress(
            UUID.randomUUID(),
            orderAddress.postalCode(),
            orderAddress.city(),
            orderAddress.street()
        );
    }

    private List<OrderItem> toOrderItemEntities(List<CreateOrderCommand.OrderItem> orderItems) {
        return orderItems.stream()
            .map(orderItem -> OrderItem.builder()
                    .product(new Product(new ProductId(orderItem.productId())))
                    .price(new Money(orderItem.price()))
                    .quantity(orderItem.quantity())
                    .subTotal(new Money(orderItem.subTotal()))
                    .build())
            .toList();
    }

    public Store toStore(CreateOrderCommand createOrderCommand) {
        return Store.builder()
            .storeId(new StoreId(createOrderCommand.customerId()))
            .products(createOrderCommand.items().stream()
                .map(orderItem -> new Product(new ProductId(orderItem.productId())))
                .toList())
            .build();
    }

    public OrderApprovalEventPayload toOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return new OrderApprovalEventPayload(
            orderPaidEvent.getOrder().getId().getValue().toString(),
            orderPaidEvent.getOrder().getStoreId().getValue().toString(),
            StoreOrderStatus.PAID.name(),
            orderPaidEvent.getOrder().getPrice().getAmount(),
            orderPaidEvent.getCreatedAt(),
            orderPaidEvent.getOrder().getItems().stream()
                .map(orderItem -> new OrderApprovalEventProduct(
                    orderItem.getProduct().getId().getValue().toString(),
                    orderItem.getQuantity()))
                .toList()
        );
    }
}
