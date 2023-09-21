package me.jincrates.ecommerce.order.service;

import static me.jincrates.ecommerce.domain.DomainConstants.UTC;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.domain.valueobject.ProductId;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.entity.Product;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.domain.event.OrderCancelledEvent;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.domain.event.OrderPaidEvent;
import me.jincrates.ecommerce.order.domain.exception.OrderDomainException;

@Slf4j
public class OrderDomainService implements OrderDomainUseCase {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Store store) {
        validateStore(store);
        setOrderProductInformation(order, store);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, getCreatedAt());
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, getCreatedAt());
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, getCreatedAt());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateStore(Store store) {
        if (!store.isActive()) {
            throw new OrderDomainException("Store with id " + store.getId().getValue() +
                " is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Store store) {
        // 매장의 상품 목록을 productId를 키로 사용하여 Map으로 변환합니다.
        Map<ProductId, Product> storeProductMap = store.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // 주문 항목의 상품이 매장의 상품 Map에 있는지 확인하고 정보를 업데이트합니다.
        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            if (storeProductMap.containsKey(currentProduct.getId())) {
                Product matchedProduct = storeProductMap.get(currentProduct.getId());
                currentProduct.updateWithConfirmedNameAndPrice(matchedProduct.getName(),
                    matchedProduct.getPrice());
            }
        });
    }

    private ZonedDateTime getCreatedAt() {
        return ZonedDateTime.now(ZoneId.of(UTC));
    }
}
