package me.jincrates.ecommerce.order.port.input.message.listener;

import me.jincrates.ecommerce.order.service.response.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
