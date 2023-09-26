package me.jincrates.ecommerce.order.service.handler.helper;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.order.domain.entity.Customer;
import me.jincrates.ecommerce.order.domain.entity.Order;
import me.jincrates.ecommerce.order.domain.entity.Store;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.domain.exception.OrderDomainException;
import me.jincrates.ecommerce.order.port.output.persistence.CustomerPort;
import me.jincrates.ecommerce.order.port.output.persistence.OrderPort;
import me.jincrates.ecommerce.order.port.output.persistence.StorePort;
import me.jincrates.ecommerce.order.service.OrderDomainUseCase;
import me.jincrates.ecommerce.order.service.handler.mapper.OrderDataMapper;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainUseCase orderDomainUseCase;
    private final OrderPort orderPort;
    private final CustomerPort customerPort;
    private final StorePort storePort;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        getVerifiedCustomer(createOrderCommand.customerId());
        Store store = getVerifiedStore(createOrderCommand);
        Order order = orderDataMapper.toOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainUseCase.validateAndInitiateOrder(order,
            store);
        saveOrder(order);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private Customer getVerifiedCustomer(UUID customerId) {
        return customerPort.findCustomer(customerId)
            .orElseThrow(() -> {
                log.warn("Could not find customer with customer id: {}", customerId);
                return new OrderDomainException(
                    "Could not find customer with customer id: " + customerId);
            });
    }
    
    private Store getVerifiedStore(CreateOrderCommand createOrderCommand) {
        Store store = orderDataMapper.toStore(createOrderCommand);
        return storePort.findStoreInformation(store)
            .orElseThrow(() -> {
                log.warn("Could not find store with store id: {}", createOrderCommand.storeId());
                return new OrderDomainException(
                    "Could not find store with store id: " + createOrderCommand.storeId());
            });
    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderPort.save(order);
        log.info("Order is saved with id: {}", savedOrder.getId().getValue());
        return savedOrder;
    }
}
