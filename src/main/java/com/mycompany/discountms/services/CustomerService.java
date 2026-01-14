package com.mycompany.discountms.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cache.annotation.CachePut;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.repository.CustomerRepository;

/**
 *
 * @author Yaqoub Alshatti
 */
@Service
public class CustomerService {
    // repository class reference
    private final CustomerRepository cusRepo;

    @Autowired
    public CustomerService(CustomerRepository cusRepo) {
        this.cusRepo = cusRepo;
    }
    
    @Async
    public List<Customer> getAllCustomers() {
        return cusRepo.findAll();
    }

    // @CachePut(cacheNames = "customers")
    // public CompletableFuture<List<Customer>> getAllCustomers() {
    //     List<Customer> all = cusRepo.findAll();
    //     return CompletableFuture.completedFuture(all);
    // }

    @Async
    public Customer getCustomerById(Long id) {
        return cusRepo.findById(id).orElse(null);
    }
    
    // public CompletableFuture<Customer> getCustomerById(Long id) {
    //     return CompletableFuture.supplyAsync(() ->
    //             cusRepo.findById(id).orElse(null));
    // }
}

