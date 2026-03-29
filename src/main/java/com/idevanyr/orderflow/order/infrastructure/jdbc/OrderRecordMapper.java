package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderStatus;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

final class OrderRecordMapper {

    static OrderData toData(Order order) {
        var data = new OrderData();
        data.setId(order.id());
        data.setVersion(order.version());
        data.setCustomerId(order.customerId());
        data.setStatus(order.status().name());
        data.setItems(order.items().stream()
                .map(item -> {
                    var itemData = new OrderItemData();
                    itemData.setProductCode(item.productCode());
                    itemData.setQuantity(item.quantity());
                    itemData.setUnitPrice(item.unitPrice());
                    return itemData;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return data;
    }

    static Order toDomain(OrderData data) {
        var items = data.getItems().stream()
                .map(item -> new OrderItem(item.getProductCode(), item.getQuantity(), item.getUnitPrice()))
                .toList();

        return new Order(
                data.getId(),
                data.getVersion(),
                data.getCustomerId(),
                items,
                OrderStatus.valueOf(data.getStatus())
        );
    }

    private OrderRecordMapper() {
    }
}
