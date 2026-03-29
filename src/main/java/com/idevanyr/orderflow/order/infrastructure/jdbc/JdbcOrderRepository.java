package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JdbcOrderRepository implements OrderRepository {

    private final SpringDataJdbcOrderCrudRepository crudRepository;

    JdbcOrderRepository(SpringDataJdbcOrderCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Order save(Order order) {
        var saved = crudRepository.save(OrderRecordMapper.toData(order));
        return OrderRecordMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return crudRepository.findById(id)
                .map(OrderRecordMapper::toDomain);
    }
}
