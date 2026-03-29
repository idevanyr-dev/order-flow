package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.ConfirmOrderResult;
import com.idevanyr.orderflow.order.application.ConfirmOrderUseCase;
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

@WebMvcTest(ConfirmOrderController.class)
class ConfirmOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfirmOrderUseCase confirmOrderUseCase;

    @Test
    void shouldReturnNoContentWhenConfirmationSucceeds() throws Exception {
        when(confirmOrderUseCase.execute(any()))
                .thenReturn(new ConfirmOrderResult.Success());

        mockMvc.perform(post("/orders/1/confirmation"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(confirmOrderUseCase.execute(any()))
                .thenReturn(new ConfirmOrderResult.NotFound());

        mockMvc.perform(post("/orders/1/confirmation"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenConfirmationIsRejected() throws Exception {
        when(confirmOrderUseCase.execute(any()))
                .thenReturn(new ConfirmOrderResult.Rejected("order is already confirmed"));

        mockMvc.perform(post("/orders/1/confirmation"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.reason").value("order is already confirmed"));
    }
}
