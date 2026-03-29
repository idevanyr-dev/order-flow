package com.idevanyr.orderflow.order.application;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsView(
        Long orderId,
        String customerId,
        String status,
        BigDecimal totalAmount,
        int totalItemsQuantity,
        List<OrderItemView> items
) {}
