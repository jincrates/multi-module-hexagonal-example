package me.jincrates.ecommerce.order.dataaccess.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jincrates.ecommerce.domain.valueobject.OrderStatus;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID storeId;
    private UUID trackingId;
    private BigInteger price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Builder
    private OrderEntity(UUID id, UUID customerId, UUID storeId, UUID trackingId,
        BigInteger price, OrderStatus orderStatus, String failureMessages,
        OrderAddressEntity address, List<OrderItemEntity> items) {
        this.id = id;
        this.customerId = customerId;
        this.storeId = storeId;
        this.trackingId = trackingId;
        this.price = price;
        this.orderStatus = orderStatus;
        this.failureMessages = failureMessages;
        this.address = address;
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
