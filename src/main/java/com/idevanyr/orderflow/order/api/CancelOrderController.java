package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.CancelOrderCommand;
import com.idevanyr.orderflow.order.application.CancelOrderResult;
import com.idevanyr.orderflow.order.application.CancelOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class CancelOrderController {

    private final CancelOrderUseCase cancelOrderUseCase;

    CancelOrderController(CancelOrderUseCase cancelOrderUseCase) {
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @PostMapping("/{orderId}/cancellation")
    ResponseEntity<Object> cancel(@PathVariable Long orderId) {
        var result = cancelOrderUseCase.execute(new CancelOrderCommand(orderId));

        return switch (result) {
            case CancelOrderResult.Success _ -> ResponseEntity.noContent().build();
            case CancelOrderResult.NotFound _ -> throw new NotFoundApiException("order not found");
            case CancelOrderResult.Rejected(var reason) -> throw new RejectedApiException(reason);
        };
    }
}
