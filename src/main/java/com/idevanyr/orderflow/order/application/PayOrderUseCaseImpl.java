package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderPayment;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
class PayOrderUseCaseImpl implements PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    PayOrderUseCaseImpl(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public PayOrderResult execute(PayOrderCommand command) {
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            return new PayOrderResult.NotFound();
        }

        var payment = order.pay();

        return switch (payment) {
            case OrderPayment.Success(var paidOrder) -> {
                var authorization = paymentGateway.authorize(new PaymentAuthorizationRequest(
                        order.id(),
                        order.customerId(),
                        order.total()
                ));

                if (authorization instanceof PaymentAuthorizationResult.Rejected(var reason)) {
                    yield new PayOrderResult.Rejected(reason);
                }

                if (authorization instanceof PaymentAuthorizationResult.Failed(var reason)) {
                    yield new PayOrderResult.Failed(reason);
                }

                orderRepository.save(paidOrder);
                yield new PayOrderResult.Success();
            }
            case OrderPayment.Rejected(var reason) ->
                    new PayOrderResult.Rejected(reason);
        };
    }
}
