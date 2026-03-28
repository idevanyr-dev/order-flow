package com.idevanyr.orderflow.order.domain;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Order {

    private final Long id;
    private final String customerId;
    private final List<OrderItem> items;
    private final OrderStatus status;

    public Order(Long id, String customerId, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId, "customerId cannot be null");
        this.items = List.copyOf(items);
        this.status = Objects.requireNonNull(status, "status cannot be null");
    }

    public static Order place(PlaceOrderCommand command) {
        var items = command.items().stream()
                .map(item -> new OrderItem(item.productCode(), item.quantity(), item.unitPrice()))
                .toList();

        return new Order(
                null,
                command.customerId(),
                items,
                OrderStatus.DRAFT
        );
    }

    public BigDecimal total() {
        return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return items;
    }

    public OrderStatus status() {
        return status;
    }
}
