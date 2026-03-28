package com.idevanyr.orderflow.order.application;

import java.util.List;

public record PlaceOrderCommand(
        String customerId,
        List<PlaceOrderItemCommand> items
) {}
