package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.application.FindOrderDetailsQuery;
import com.idevanyr.orderflow.order.application.OrderDetailsView;
import com.idevanyr.orderflow.order.application.OrderItemView;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                        select id, customer_id, status
                        from orders
                        where id = :orderId
                        """)
                .param("orderId", orderId)
                .query((rs, rowNum) -> new OrderHeaderRow(
                        rs.getLong("id"),
                        rs.getString("customer_id"),
                        rs.getString("status")
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
                items
        ));
    }

    private record OrderHeaderRow(
            Long id,
            String customerId,
            String status
    ) {}
}
