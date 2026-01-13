package com.mycompany.discountms.controller;

import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.response.BillResponse;
import com.mycompany.discountms.services.BillService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


/**
 * Controller calls on BillService where discount rules are set
 * @author Yaqoub Alshatti
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillRequest request) {
        BillResponse response = billService.createAndSaveBill(request);
        return ResponseEntity.ok(response);
    }
}
