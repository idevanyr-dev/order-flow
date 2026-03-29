package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.application.FindOrderDetailsQuery;
import com.idevanyr.orderflow.order.application.OrderDetailsView;
import com.idevanyr.orderflow.order.application.OrderItemView;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JdbcFindOrderDetailsQuery implements FindOrderDetailsQuery {

    private final JdbcClient jdbcClient;

    JdbcFindOrderDetailsQuery(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<OrderDetailsView> execute(Long orderId) {
        var header = jdbcClient.sql("""
                        select o.id,
                               o.customer_id,
                               o.status,
                               coalesce(sum(oi.quantity * oi.unit_price), 0) as total_amount,
                               coalesce(sum(oi.quantity), 0) as total_items_quantity
                        from orders o
                        left join order_items oi on oi.order_id = o.id
                        where o.id = :orderId
                        group by o.id, o.customer_id, o.status
                        """)
                .param("orderId", orderId)
                .query((rs, rowNum) -> new OrderHeaderRow(
                        rs.getLong("id"),
                        rs.getString("customer_id"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount"),
                        rs.getInt("total_items_quantity")
                ))
                .optional();

        if (header.isEmpty()) {
            return Optional.empty();
        }

        var items = jdbcClient.sql("""
                        select product_code, quantity, unit_price
                        from order_items
                        where order_id = :orderId
                        order by product_code
                        """)
                .param("orderId", orderId)
                .query((rs, rowNum) -> new OrderItemView(
                        rs.getString("product_code"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price")
                ))
                .list();

        var order = header.get();
        return Optional.of(new OrderDetailsView(
                order.id(),
                order.customerId(),
                order.status(),
                order.totalAmount(),
                order.totalItemsQuantity(),
                items
        ));
    }

    private record OrderHeaderRow(
            Long id,
            String customerId,
            String status,
            java.math.BigDecimal totalAmount,
            int totalItemsQuantity
    ) {}
}
