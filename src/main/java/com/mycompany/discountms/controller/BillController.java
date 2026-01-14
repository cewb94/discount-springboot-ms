package com.mycompany.discountms.controller;

import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.response.BillResponse;

import com.mycompany.discountms.services.BillService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller calls on BillService where discount rules are set
 * 
 * @author Yaqoub Alshatti
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    /**
     * 
     * @param request DTO object created by Jackson from HTTP POST JSON
     * @return BillResponse DTO which then gets converted to equivalent JSON
     */
    @PostMapping("/add")
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillRequest request) {
        BillResponse response = billService.createAndSaveBill(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BillResponse>> getAllBills() {
        List<BillResponse> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable("id") Long id) {
        BillResponse bill = billService.getBillById(id);

        return bill != null
                ? ResponseEntity.ok(bill)
                : ResponseEntity.notFound().build();
    }
}
