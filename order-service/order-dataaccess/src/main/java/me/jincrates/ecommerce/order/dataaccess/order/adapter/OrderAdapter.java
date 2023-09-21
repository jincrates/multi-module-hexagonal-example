package me.jincrates.ecommerce.order.dataaccess.order.adapter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jincrates.ecommerce.domain.valueobject.OrderId;
import me.jincrates.ecommerce.order.dataaccess.order.mapper.OrderDataAccessMapper;
import me.jincrates.ecommerce.order.dataaccess.order.repository.OrderJpaRepository;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.valueobject.TrackingId;
import me.jincrates.ecommerce.order.port.output.persistence.OrderPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.toOrder(orderJpaRepository
            .save(orderDataAccessMapper.toOrderEntity(order)));
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.getValue())
            .map(orderDataAccessMapper::toOrder);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
            .map(orderDataAccessMapper::toOrder);
    }
}
