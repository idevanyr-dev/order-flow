package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderPolicy;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class PlaceOrderUseCaseImpl implements PlaceOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(PlaceOrderUseCaseImpl.class);

    private final OrderPolicy orderPolicy;
    private final OrderRepository orderRepository;
    private final NotificationGateway notificationGateway;

    PlaceOrderUseCaseImpl(OrderPolicy orderPolicy, OrderRepository orderRepository, NotificationGateway notificationGateway) {
        this.orderPolicy = orderPolicy;
        this.orderRepository = orderRepository;
        this.notificationGateway = notificationGateway;
    }

    @Override
    public PlacedOrderResult execute(PlaceOrderCommand command) {
        log.info("Placing order for customerId={} with {} item(s)", command.customerId(), command.items().size());

        var errors = orderPolicy.validateForPlacement(command);
        if (!errors.isEmpty()) {
            log.warn("Order placement rejected by validation for customerId={}: {}", command.customerId(), errors);
            return new PlacedOrderResult.ValidationError(errors);
        }

        var order = Order.place(command);
        var savedOrder = orderRepository.save(order);
        notificationGateway.notify(new OrderNotification(
                OrderNotification.Type.ORDER_PLACED,
                savedOrder.id(),
                savedOrder.customerId(),
                savedOrder.total()
        ));
        log.info("Order placed successfully with orderId={}", savedOrder.id());

        return new PlacedOrderResult.Success(savedOrder.id());
    }
}
