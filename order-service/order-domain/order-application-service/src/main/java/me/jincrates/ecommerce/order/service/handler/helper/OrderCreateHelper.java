package me.jincrates.ecommerce.order.service.handler.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.order.domain.event.OrderCreatedEvent;
import me.jincrates.ecommerce.order.port.output.persistence.CustomerPort;
import me.jincrates.ecommerce.order.port.output.persistence.OrderPort;
import me.jincrates.ecommerce.order.port.output.persistence.StorePort;
import me.jincrates.ecommerce.order.service.OrderDomainUseCase;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainUseCase orderDomainUseCase;
    private final OrderPort orderPort;
    private final CustomerPort customerPort;
    private final StorePort storePort;

    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        return null;
    }
}
