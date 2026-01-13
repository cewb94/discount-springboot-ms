package com.mycompany.discountms.controller;


import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.services.CustomerService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yaqoub Alshatti
 */
@Controller
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;
    
    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }
    
    
    @GetMapping
    public CompletableFuture<ResponseEntity<List<Customer>>> getAllCustomers() {
        return service.getAllCustomers().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerById(@PathVariable("id") Long id) {
        return service.getCustomerById(id).thenApply(j -> j != null
                ? ResponseEntity.ok(j)
                : ResponseEntity.notFound().build());
    }

}
