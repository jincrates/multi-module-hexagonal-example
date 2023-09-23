package me.jincrates.ecommerce.order.dataaccess.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_items")
@IdClass(OrderItemEntityId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemEntity {
    @Id
    private Long id;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

    @Builder
    private OrderItemEntity(Long id, OrderEntity order, UUID productId, BigDecimal price,
        Integer quantity, BigDecimal subTotal) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

}
