package me.jincrates.ecommerce.order.dataaccess.order.mapper;

import static me.jincrates.ecommerce.domain.DomainConstants.FAILURE_MESSAGE_DELIMITER;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.jincrates.ecommerce.domain.valueobject.CustomerId;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.OrderId;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.domain.valueobject.StoreId;
import me.jincrates.ecommerce.order.dataaccess.order.entity.OrderAddressEntity;
import me.jincrates.ecommerce.order.dataaccess.order.entity.OrderEntity;
import me.jincrates.ecommerce.order.dataaccess.order.entity.OrderItemEntity;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.entity.OrderItem;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.valueobject.OrderItemId;
import me.jincrates.ecommerce.order.domain.valueobject.StreetAddress;
import me.jincrates.ecommerce.order.domain.valueobject.TrackingId;

public class OrderDataAccessMapper {

    public OrderEntity toOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
            .id(order.getId().getValue())
            .customerId(order.getCustomerId().getValue())
            .storeId(order.getStoreId().getValue())
            .trackingId(order.getTrackingId().getValue())
            .address(toAddressEntity(order.getDeliveryAddress()))
            .price(order.getPrice().getAmount())
            .items(toOrderItemEntities(order.getItems()))
            .orderStatus(order.getOrderStatus())
            .failureMessages(order.getFailureMessages() != null ?
                String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
            .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order toOrder(OrderEntity orderEntity) {
        return Order.builder()
            .orderId(new OrderId(orderEntity.getId()))
            .customerId(new CustomerId(orderEntity.getCustomerId()))
            .storeId(new StoreId(orderEntity.getStoreId()))
            .deliveryAddress(toDeliveryAddress(orderEntity.getAddress()))
            .price(new Money(orderEntity.getPrice()))
            .items(toOrderItems(orderEntity.getItems()))
            .trackingId(new TrackingId(orderEntity.getTrackingId()))
            .orderStatus(orderEntity.getOrderStatus())
            .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                new ArrayList<>(List.of(orderEntity.getFailureMessages())))
            .build();
    }

    private OrderAddressEntity toAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
            .id(deliveryAddress.id())
            .postalCode(deliveryAddress.postalCode())
            .city(deliveryAddress.city())
            .street(deliveryAddress.street())
            .build();
    }

    private List<OrderItemEntity> toOrderItemEntities(List<OrderItem> items) {
        return items.stream()
            .map(orderItem -> OrderItemEntity.builder()
                .id(orderItem.getId().getValue())
                .productId(orderItem.getProduct().getId().getValue())
                .price(orderItem.getPrice().getAmount())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal().getAmount())
                .build())
            .collect(Collectors.toList());
    }

    private StreetAddress toDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(
            address.getId(),
            address.getPostalCode(),
            address.getCity(),
            address.getStreet()
        );
    }

    private List<OrderItem> toOrderItems(List<OrderItemEntity> items) {
        return items.stream()
            .map(orderItemEntity -> OrderItem.builder()
                .orderItemId(new OrderItemId(orderItemEntity.getId()))
                .product(new Product(new ProductId(orderItemEntity.getProductId())))
                .price(new Money(orderItemEntity.getPrice()))
                .quantity(orderItemEntity.getQuantity())
                .subTotal(new Money(orderItemEntity.getSubTotal()))
                .build())
            .collect(Collectors.toList());
    }

}
