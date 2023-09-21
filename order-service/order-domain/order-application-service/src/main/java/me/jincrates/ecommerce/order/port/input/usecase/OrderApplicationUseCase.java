package me.jincrates.ecommerce.order.port.input.usecase;

import jakarta.validation.Valid;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import me.jincrates.ecommerce.order.service.request.TrackOrderQuery;
import me.jincrates.ecommerce.order.service.response.CreateOrderResponse;
import me.jincrates.ecommerce.order.service.response.TrackOrderResponse;

public interface OrderApplicationUseCase {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
