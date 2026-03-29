package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PayOrderCommand;
import com.idevanyr.orderflow.order.application.PayOrderResult;
import com.idevanyr.orderflow.order.application.PayOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class PayOrderController {

    private final PayOrderUseCase payOrderUseCase;

    PayOrderController(PayOrderUseCase payOrderUseCase) {
        this.payOrderUseCase = payOrderUseCase;
    }

    @PostMapping("/{orderId}/payment")
    ResponseEntity<Object> pay(@PathVariable Long orderId) {
        var result = payOrderUseCase.execute(new PayOrderCommand(orderId));

        return switch (result) {
            case PayOrderResult.Success _ -> ResponseEntity.noContent().build();
            case PayOrderResult.NotFound _ -> throw new NotFoundApiException("order not found");
            case PayOrderResult.Rejected(var reason) -> throw new RejectedApiException(reason);
            case PayOrderResult.Failed(var reason) -> throw new UpstreamFailureApiException(reason);
        };
    }
}
