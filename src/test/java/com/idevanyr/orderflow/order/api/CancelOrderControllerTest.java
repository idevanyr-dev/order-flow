package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.CancelOrderResult;
import com.idevanyr.orderflow.order.application.CancelOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CancelOrderController.class)
class CancelOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CancelOrderUseCase cancelOrderUseCase;

    @Test
    void shouldReturnNoContentWhenCancellationSucceeds() throws Exception {
        when(cancelOrderUseCase.execute(any()))
                .thenReturn(new CancelOrderResult.Success());

        mockMvc.perform(post("/orders/1/cancellation"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(cancelOrderUseCase.execute(any()))
                .thenReturn(new CancelOrderResult.NotFound());

        mockMvc.perform(post("/orders/1/cancellation"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenCancellationIsRejected() throws Exception {
        when(cancelOrderUseCase.execute(any()))
                .thenReturn(new CancelOrderResult.Rejected("paid order cannot be cancelled"));

        mockMvc.perform(post("/orders/1/cancellation"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.reason").value("paid order cannot be cancelled"));
    }
}
