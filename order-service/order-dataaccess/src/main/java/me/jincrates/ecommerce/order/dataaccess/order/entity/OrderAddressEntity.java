package me.jincrates.ecommerce.order.dataaccess.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_address")
public class OrderAddressEntity {

    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private String postalCode;
    private String city;
    private String street;

    @Builder
    private OrderAddressEntity(UUID id, OrderEntity order, String postalCode, String city,
        String street) {
        this.id = id;
        this.order = order;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressEntity that = (OrderAddressEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}