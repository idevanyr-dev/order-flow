package com.idevanyr.orderflow.order.application;

import java.util.Optional;

public interface FindOrderDetailsQuery {
    Optional<OrderDetailsView> execute(Long orderId);
}
