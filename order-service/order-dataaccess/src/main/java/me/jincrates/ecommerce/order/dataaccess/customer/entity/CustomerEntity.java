package me.jincrates.ecommerce.order.dataaccess.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "customer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerEntity {
    @Id
    private UUID id;
    private String email;
    private String name;

    @Builder
    private CustomerEntity(UUID id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
