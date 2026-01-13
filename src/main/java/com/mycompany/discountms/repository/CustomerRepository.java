package com.mycompany.discountms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mycompany.discountms.entity.Customer;

/**
 *
 * @author Yaqoub Alshatti
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // example native query
    @Query(
        value = "select * from discount.customers cus where cus.cus_full_name like concat('%', ?1, '%')",
        nativeQuery = true
    )
    List<Customer> findByName(String cusFullName);

    // automatically embedded methods through JpaRepository<Customer, Long> interface
    /*
     * findById(Long id)
     * findAll()
     * save(Customer c)
     * deleteById(Long id)
     * existsById(Long id)
     * count()
     */
}
