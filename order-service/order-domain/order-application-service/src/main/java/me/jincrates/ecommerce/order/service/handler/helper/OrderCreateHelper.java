package me.jincrates.ecommerce.order.service.handler.helper;

import java.util.Optional;
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
        checkCustomer(createOrderCommand.customerId());
        Store store = checkStore(createOrderCommand);
        Order order = orderDataMapper.toOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainUseCase.validateAndInitiateOrder(order, store);
        saveOrder(order);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerPort.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Store checkStore(CreateOrderCommand createOrderCommand) {
        Store store = orderDataMapper.toStore(createOrderCommand);
        Optional<Store> optionalStore = storePort.findStoreInformation(store);
        if (optionalStore.isEmpty()) {
            log.warn("Could not find store with store id: {}", createOrderCommand.storeId());
            throw new OrderDomainException("Could not find store with store id: " + createOrderCommand.storeId());
        }
        return optionalStore.get();
    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderPort.save(order);
        if (savedOrder == null) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order ist saved with id: {}", savedOrder.getId().getValue());
        return savedOrder;
    }
}
