package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderPayment;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
class PayOrderUseCaseImpl implements PayOrderUseCase {

    private final OrderRepository orderRepository;

    PayOrderUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
                orderRepository.save(paidOrder);
                yield new PayOrderResult.Success();
            }
            case OrderPayment.Rejected(var reason) ->
                    new PayOrderResult.Rejected(reason);
        };
    }
}
