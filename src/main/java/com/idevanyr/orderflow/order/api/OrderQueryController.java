package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.FindOrderDetailsQuery;
import com.idevanyr.orderflow.order.application.OrderDetailsView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrderQueryController {

    private final FindOrderDetailsQuery findOrderDetailsQuery;

    OrderQueryController(FindOrderDetailsQuery findOrderDetailsQuery) {
        this.findOrderDetailsQuery = findOrderDetailsQuery;
    }

    @GetMapping("/{orderId}")
    ResponseEntity<OrderDetailsView> findById(@PathVariable Long orderId) {
        return findOrderDetailsQuery.execute(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
