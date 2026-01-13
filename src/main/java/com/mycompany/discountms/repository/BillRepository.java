package com.mycompany.discountms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.discountms.entity.Bill;


/**
 *
 * @author Yaqoub Alshatti
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
}