package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.FindOrderDetailsQuery;
import com.idevanyr.orderflow.order.application.OrderDetailsView;
import com.idevanyr.orderflow.order.application.OrderItemView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderQueryController.class)
class OrderQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindOrderDetailsQuery findOrderDetailsQuery;

    @Test
    void shouldReturnOrderDetailsWhenOrderExists() throws Exception {
        when(findOrderDetailsQuery.execute(1L))
                .thenReturn(Optional.of(new OrderDetailsView(
                        1L,
                        "C-100",
                        "PLACED",
                        List.of(
                                new OrderItemView("P-10", 2, new BigDecimal("49.90")),
                                new OrderItemView("P-20", 1, new BigDecimal("19.90"))
                        )
                )));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerId").value("C-100"))
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.items[0].productCode").value("P-10"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].unitPrice").value(49.90))
                .andExpect(jsonPath("$.items[1].productCode").value("P-20"));
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(findOrderDetailsQuery.execute(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());
    }
}
