package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderConfirmation;
import com.idevanyr.orderflow.order.domain.OrderRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
class ConfirmOrderUseCaseImpl implements ConfirmOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderUseCaseImpl.class);
    private static final String CONFLICT_REASON = "order was changed by another request";

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
                final var savedOrder = trySaveConfirmedOrder(command, confirmedOrder);
                if (savedOrder.isEmpty()) {
                    yield new ConfirmOrderResult.Conflict(CONFLICT_REASON);
                }
                notificationGateway.notify(new OrderNotification(
                        OrderNotification.Type.ORDER_CONFIRMED,
                        savedOrder.get().id(),
                        savedOrder.get().customerId(),
                        savedOrder.get().total()
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

    private Optional<Order> trySaveConfirmedOrder(
            ConfirmOrderCommand command,
            Order confirmedOrder
    ) {
        try {
            return Optional.of(orderRepository.save(confirmedOrder));
        } catch (OptimisticLockingFailureException _) {
            log.warn("Order confirmation conflicted for orderId={}", command.orderId());
            return Optional.empty();
        }
    }
}
