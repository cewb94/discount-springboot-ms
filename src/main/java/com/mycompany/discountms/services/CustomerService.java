package com.mycompany.discountms.services;


import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    // @CachePut(cacheNames = "customers")
    public CompletableFuture<List<Customer>> getAllCustomers() {
        List<Customer> all = cusRepo.findAll();
        return CompletableFuture.completedFuture(all);
    }

    public CompletableFuture<Customer> getCustomerById(Long id) {
        return CompletableFuture.supplyAsync(() ->
                cusRepo.findById(id).orElse(null));
    }
    
    
}

