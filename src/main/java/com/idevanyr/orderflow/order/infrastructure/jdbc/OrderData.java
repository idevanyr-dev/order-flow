package com.idevanyr.orderflow.order.infrastructure.jdbc;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Table("orders")
public class OrderData {

    @Id
    private Long id;
    private String customerId;
    private String status;

    @MappedCollection(idColumn = "order_id")
    private Set<OrderItemData> items = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<OrderItemData> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemData> items) {
        this.items = items;
    }
}
