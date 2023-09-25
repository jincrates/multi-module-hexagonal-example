package me.jincrates.ecommerce.order.service;

import static me.jincrates.ecommerce.domain.DomainConstants.JOINING_MESSAGE_DELIMITER;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.ecommerce.order.port.input.message.listener.PaymentResponseMessageListener;
import me.jincrates.ecommerce.order.service.handler.OrderPaymentSagaHandler;
import me.jincrates.ecommerce.order.service.response.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PaymentResponseMessageListenerService implements PaymentResponseMessageListener {

    private final OrderPaymentSagaHandler orderPaymentSagaHandler;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSagaHandler.process(paymentResponse);
        log.info("Order Payment Saga process operation is completed for order id: {}",
            paymentResponse.orderId());
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSagaHandler.rollback(paymentResponse);
        log.info("Order is roll backed for order id: {} with failure messages: {}",
            paymentResponse.orderId(),
            String.join(JOINING_MESSAGE_DELIMITER, paymentResponse.failureMessages()));
    }
}
