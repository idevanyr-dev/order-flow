package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PayOrderResult;
import com.idevanyr.orderflow.order.application.PayOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PayOrderController.class)
class PayOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PayOrderUseCase payOrderUseCase;

    @Test
    void shouldReturnNoContentWhenPaymentSucceeds() throws Exception {
        when(payOrderUseCase.execute(any()))
                .thenReturn(new PayOrderResult.Success());

        mockMvc.perform(post("/orders/1/payment"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(payOrderUseCase.execute(any()))
                .thenReturn(new PayOrderResult.NotFound());

        mockMvc.perform(post("/orders/1/payment"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenPaymentIsRejected() throws Exception {
        when(payOrderUseCase.execute(any()))
                .thenReturn(new PayOrderResult.Rejected("cancelled order cannot be paid"));

        mockMvc.perform(post("/orders/1/payment"))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.reason").value("cancelled order cannot be paid"));
    }

    @Test
    void shouldReturnBadGatewayWhenPaymentGatewayFails() throws Exception {
        when(payOrderUseCase.execute(any()))
                .thenReturn(new PayOrderResult.Failed("payment service is unavailable"));

        mockMvc.perform(post("/orders/1/payment"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.reason").value("payment service is unavailable"));
    }
}
