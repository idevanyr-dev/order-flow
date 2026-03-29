package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderCancellation;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private final OrderRepository orderRepository;

    CancelOrderUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CancelOrderResult execute(CancelOrderCommand command) {
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            return new CancelOrderResult.NotFound();
        }

        var cancellation = order.cancel();

        return switch (cancellation) {
            case OrderCancellation.Success success -> {
                orderRepository.save(success.order());
                yield new CancelOrderResult.Success();
            }
            case OrderCancellation.Rejected rejected ->
                    new CancelOrderResult.Rejected(rejected.reason());
        };
    }
}
