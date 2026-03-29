package com.idevanyr.orderflow.order.infrastructure;

import com.idevanyr.orderflow.order.application.PaymentAuthorizationRequest;
import com.idevanyr.orderflow.order.application.PaymentAuthorizationResult;
import com.idevanyr.orderflow.order.application.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
class DefaultPaymentGateway implements PaymentGateway {

    @Override
    public PaymentAuthorizationResult authorize(PaymentAuthorizationRequest request) {
        return new PaymentAuthorizationResult.Authorized();
    }
}
