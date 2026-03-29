package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.OrderCancellation;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(CancelOrderUseCaseImpl.class);

    private final OrderRepository orderRepository;
    private final NotificationGateway notificationGateway;

    CancelOrderUseCaseImpl(OrderRepository orderRepository, NotificationGateway notificationGateway) {
        this.orderRepository = orderRepository;
        this.notificationGateway = notificationGateway;
    }

    @Override
    public CancelOrderResult execute(CancelOrderCommand command) {
        log.info("Cancelling orderId={}", command.orderId());
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            log.warn("Order cancellation failed because orderId={} was not found", command.orderId());
            return new CancelOrderResult.NotFound();
        }

        var cancellation = order.cancel();

        return switch (cancellation) {
            case OrderCancellation.Success(var cancelledOrder) -> {
                var savedOrder = orderRepository.save(cancelledOrder);
                notificationGateway.notify(new OrderNotification(
                        OrderNotification.Type.ORDER_CANCELLED,
                        savedOrder.id(),
                        savedOrder.customerId(),
                        savedOrder.total()
                ));
                log.info("Order cancelled successfully for orderId={}", command.orderId());
                yield new CancelOrderResult.Success();
            }
            case OrderCancellation.Rejected(var reason) -> {
                log.warn("Order cancellation rejected for orderId={}: {}", command.orderId(), reason);
                yield new CancelOrderResult.Rejected(reason);
            }
        };
    }
}
