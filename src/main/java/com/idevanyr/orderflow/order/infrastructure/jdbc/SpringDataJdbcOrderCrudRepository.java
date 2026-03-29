package com.idevanyr.orderflow.order.infrastructure.jdbc;

import org.springframework.data.repository.CrudRepository;

public interface SpringDataJdbcOrderCrudRepository extends CrudRepository<OrderData, Long> {
}
