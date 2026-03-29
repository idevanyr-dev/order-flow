package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.ConfirmOrderCommand;
import com.idevanyr.orderflow.order.application.ConfirmOrderResult;
import com.idevanyr.orderflow.order.application.ConfirmOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
class ConfirmOrderController {

    private final ConfirmOrderUseCase confirmOrderUseCase;

    ConfirmOrderController(ConfirmOrderUseCase confirmOrderUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    @PostMapping("/{orderId}/confirmation")
    ResponseEntity<Object> confirm(@PathVariable Long orderId) {
        var result = confirmOrderUseCase.execute(new ConfirmOrderCommand(orderId));

        return switch (result) {
            case ConfirmOrderResult.Success _ -> ResponseEntity.noContent().build();
            case ConfirmOrderResult.NotFound _ -> ResponseEntity.notFound().build();
            case ConfirmOrderResult.Rejected(var reason) ->
                    ResponseEntity.status(422).body(Map.of("reason", reason));
        };
    }
}
