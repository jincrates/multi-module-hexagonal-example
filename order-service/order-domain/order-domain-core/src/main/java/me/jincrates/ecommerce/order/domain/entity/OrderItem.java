package me.jincrates.ecommerce.order.domain.entity;

import me.jincrates.ecommerce.domain.entity.BaseEntity;
import me.jincrates.ecommerce.domain.valueobject.Money;
import me.jincrates.ecommerce.domain.valueobject.OrderId;
import me.jincrates.ecommerce.order.domain.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    public OrderItem(OrderItemId orderItemId, OrderId orderId, Product product, int quantity, Money price, Money subTotal) {
        super.setId(orderItemId);
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero() &&
            price.equals(product.getPrice()) &&
            price.multiply(quantity).equals(subTotal);
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }
}
