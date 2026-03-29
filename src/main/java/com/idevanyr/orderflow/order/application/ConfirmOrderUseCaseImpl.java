package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderConfirmation;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class ConfirmOrderUseCaseImpl implements ConfirmOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderUseCaseImpl.class);

    private final OrderRepository orderRepository;
    private final NotificationGateway notificationGateway;

    ConfirmOrderUseCaseImpl(OrderRepository orderRepository, NotificationGateway notificationGateway) {
        this.orderRepository = orderRepository;
        this.notificationGateway = notificationGateway;
    }

    @Override
    public ConfirmOrderResult execute(ConfirmOrderCommand command) {
        log.info("Confirming orderId={}", command.orderId());
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            log.warn("Order confirmation failed because orderId={} was not found", command.orderId());
            return new ConfirmOrderResult.NotFound();
        }

        var confirmation = order.confirm();

        return switch (confirmation) {
            case OrderConfirmation.Success(var confirmedOrder) -> {
                var savedOrder = orderRepository.save(confirmedOrder);
                notificationGateway.notify(new OrderNotification(
                        OrderNotification.Type.ORDER_CONFIRMED,
                        savedOrder.id(),
                        savedOrder.customerId(),
                        savedOrder.total()
                ));
                log.info("Order confirmed successfully for orderId={}", command.orderId());
                yield new ConfirmOrderResult.Success();
            }
            case OrderConfirmation.Rejected(var reason) -> {
                log.warn("Order confirmation rejected for orderId={}: {}", command.orderId(), reason);
                yield new ConfirmOrderResult.Rejected(reason);
            }
        };
    }
}
