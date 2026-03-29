package com.idevanyr.orderflow.order.application;

import java.util.List;

public record OrderDetailsView(
        Long orderId,
        String customerId,
        String status,
        List<OrderItemView> items
) {}
