package me.jincrates.ecommerce.order.application.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.order.port.input.usecase.OrderApplicationUseCase;
import me.jincrates.ecommerce.order.service.request.CreateOrderCommand;
import me.jincrates.ecommerce.order.service.request.TrackOrderQuery;
import me.jincrates.ecommerce.order.service.response.CreateOrderResponse;
import me.jincrates.ecommerce.order.service.response.TrackOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문 서비스", description = "주문 생성/조회 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderRestController {
    private final OrderApplicationUseCase orderApplicationUseCase;

    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
        @RequestBody CreateOrderCommand createOrderCommand
    ) {
        log.info("Creating order for customer: {} at store: {}", createOrderCommand.customerId(),
            createOrderCommand.storeId());
        CreateOrderResponse createOrderResponse = orderApplicationUseCase.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", createOrderResponse.orderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @Operation(summary = "주문 조회")
    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(
        @PathVariable(name = "trackingId") UUID trackingId
    ) {
        TrackOrderResponse trackOrderResponse = orderApplicationUseCase.trackOrder(
            new TrackOrderQuery(trackingId));
        log.info("Returning order status with tracking id: {}", trackOrderResponse.orderTrackingId());
        return ResponseEntity.ok(trackOrderResponse);
    }
}
