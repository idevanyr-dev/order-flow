package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderPayment;
import com.idevanyr.orderflow.order.domain.OrderRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
class PayOrderUseCaseImpl implements PayOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(PayOrderUseCaseImpl.class);
    private static final String CONFLICT_REASON = "order was changed by another request";

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    private final NotificationGateway notificationGateway;

    PayOrderUseCaseImpl(
            OrderRepository orderRepository,
            PaymentGateway paymentGateway,
            NotificationGateway notificationGateway
    ) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
        this.notificationGateway = notificationGateway;
    }

    @Override
    public PayOrderResult execute(PayOrderCommand command) {
        log.info("Paying orderId={}", command.orderId());
        var order = orderRepository.findById(command.orderId()).orElse(null);

        if (order == null) {
            log.warn("Order payment failed because orderId={} was not found", command.orderId());
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
                    log.warn("Order payment rejected by gateway for orderId={}: {}", command.orderId(), reason);
                    yield new PayOrderResult.Rejected(reason);
                }

                if (authorization instanceof PaymentAuthorizationResult.Failed(var reason)) {
                    log.error("Order payment failed due to gateway error for orderId={}: {}", command.orderId(), reason);
                    yield new PayOrderResult.Failed(reason);
                }

                final var savedOrder = trySavePaidOrder(command, paidOrder);
                if (savedOrder.isEmpty()) {
                    yield new PayOrderResult.Conflict(CONFLICT_REASON);
                }
                notificationGateway.notify(new OrderNotification(
                        OrderNotification.Type.ORDER_PAID,
                        savedOrder.get().id(),
                        savedOrder.get().customerId(),
                        savedOrder.get().total()
                ));
                log.info("Order paid successfully for orderId={}", command.orderId());
                yield new PayOrderResult.Success();
            }
            case OrderPayment.Rejected(var reason) -> {
                log.warn("Order payment rejected by domain for orderId={}: {}", command.orderId(), reason);
                yield
                    new PayOrderResult.Rejected(reason);
            }
        };
    }

    private Optional<Order> trySavePaidOrder(
            PayOrderCommand command,
            Order paidOrder
    ) {
        try {
            return Optional.of(orderRepository.save(paidOrder));
        } catch (OptimisticLockingFailureException _) {
            log.warn("Order payment conflicted for orderId={}", command.orderId());
            return Optional.empty();
        }
    }
}
