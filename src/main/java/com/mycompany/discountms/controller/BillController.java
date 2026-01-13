package com.mycompany.discountms.controller;

import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.response.BillResponse;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;

/**
 *
 * @author Yaqoub Alshatti
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {

    @PostMapping
    public BillResponse createBill(@Valid @RequestBody BillRequest request) {

        // Dummy response just to test API wiring
        return new BillResponse(
            request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())),
            request.getPrice(),
            BigDecimal.ZERO,
            "NONE",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
        );
    }
}
