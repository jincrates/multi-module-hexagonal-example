package me.jincrates.ecommerce.order.service.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.exception.OrderNotFoundException;
import me.jincrates.ecommerce.order.domain.valueobject.TrackingId;
import me.jincrates.ecommerce.order.port.output.persistence.OrderPort;
import me.jincrates.ecommerce.order.service.handler.mapper.OrderDataMapper;
import me.jincrates.ecommerce.order.service.request.TrackOrderQuery;
import me.jincrates.ecommerce.order.service.response.TrackOrderResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {
    private final OrderDataMapper orderDataMapper;
    private final OrderPort orderPort;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderResult = orderPort.findByTrackingId(
            new TrackingId(trackOrderQuery.orderTrackingId()));
        if (orderResult.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.orderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " +
                trackOrderQuery.orderTrackingId());
        }
        return orderDataMapper.toTrackOrderResponse(orderResult.get());
    }
}
