package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderPolicy;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
class PlaceOrderUseCaseImpl implements PlaceOrderUseCase {

    private final OrderPolicy orderPolicy;
    private final OrderRepository orderRepository;

    PlaceOrderUseCaseImpl(OrderPolicy orderPolicy, OrderRepository orderRepository) {
        this.orderPolicy = orderPolicy;
        this.orderRepository = orderRepository;
    }

    @Override
    public PlacedOrderResult execute(PlaceOrderCommand command) {
        var errors = orderPolicy.validateForPlacement(command);
        if (!errors.isEmpty()) {
            return new PlacedOrderResult.ValidationError(errors);
        }

        var order = Order.place(command);
        var savedOrder = orderRepository.save(order);

        return new PlacedOrderResult.Success(savedOrder.id());
    }
}
