package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderConfirmation;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
class ConfirmOrderUseCaseImpl implements ConfirmOrderUseCase {

    private final OrderRepository orderRepository;

    ConfirmOrderUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ConfirmOrderResult execute(ConfirmOrderCommand command) {
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            return new ConfirmOrderResult.NotFound();
        }

        var confirmation = order.confirm();

        return switch (confirmation) {
            case OrderConfirmation.Success(var confirmedOrder) -> {
                orderRepository.save(confirmedOrder);
                yield new ConfirmOrderResult.Success();
            }
            case OrderConfirmation.Rejected(var reason) ->
                    new ConfirmOrderResult.Rejected(reason);
        };
    }
}
