package com.mycompany.discountms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.discountms.dto.request.BillLineRequest;
import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.response.BillResponse;
import com.mycompany.discountms.enums.ItemCategory;
import com.mycompany.discountms.services.BillService;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean BillService billService;


    /*
    POST /api/bills/add

    Request:
    {
        "customerId": 1,
        "lines": [
            { "itmId": 1,  "itmCategory": "GROCERY",     "price": 2.00,  "quantity": 3 },
            { "itmId": 11, "itmCategory": "NON_GROCERY", "price": 10.00, "quantity": 1 }
        ]
    }

    Expected Response:
    {
        "originalAmount": 16.00,
        "groceryAmount": 6.00,
        "nonGroceryAmount": 10.00,
        "appliedPercentageDiscount": "EMPLOYEE",
        "percentageDiscountAmount": 3.00,
        "flatDiscountAmount": 0.00,
        "totalDiscount": 3.00,
        "netPayableAmount": 13.00
    }
    */
    @Test
    void postAdd_returnsBillResponse() throws Exception {
        BillResponse response = new BillResponse(
                new BigDecimal("16.00"),
                new BigDecimal("6.00"),
                new BigDecimal("10.00"),
                "EMPLOYEE",
                new BigDecimal("3.00"),
                new BigDecimal("0.00"),
                new BigDecimal("3.00"),
                new BigDecimal("13.00")
        );

        when(billService.createAndSaveBill(any())).thenReturn(response);

        BillLineRequest l1 = new BillLineRequest();
        l1.setItmId(1L);
        l1.setItmCategory(ItemCategory.GROCERY);
        l1.setPrice(new BigDecimal("2.00"));
        l1.setQuantity(3);

        BillLineRequest l2 = new BillLineRequest();
        l2.setItmId(11L);
        l2.setItmCategory(ItemCategory.NON_GROCERY);
        l2.setPrice(new BigDecimal("10.00"));
        l2.setQuantity(1);

        BillRequest req = new BillRequest();
        req.setCustomerId(1L);
        req.setLines(List.of(l1, l2));

        mvc.perform(post("/api/bills/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalAmount").value(16.00))
            .andExpect(jsonPath("$.appliedPercentageDiscount").value("EMPLOYEE"))
            .andExpect(jsonPath("$.netPayableAmount").value(13.00));

        verify(billService).createAndSaveBill(any(BillRequest.class));
    }

    @Test
    void postAdd_invalidPayload_returns400() throws Exception {
        // Missing customerId + lines -> should fail validation (@Valid)
        String badJson = "{}";

        mvc.perform(post("/api/bills/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(billService);
    }
}
