package com.mycompany.discountms.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.request.BillLineRequest;
import com.mycompany.discountms.dto.response.BillResponse;
import com.mycompany.discountms.entity.Bill;
import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.enums.CustomerType;
import com.mycompany.discountms.enums.ItemCategory;
import com.mycompany.discountms.repository.BillRepository;
import com.mycompany.discountms.repository.CustomerRepository;

@Service
public class BillService {

    final static String EMP_DISCOUNT_PERCENT = "0.30";
    final static String AFF_DISCOUNT_PERCENT = "0.10";
    final static String LOY_DISCOUNT_PERCENT = "0.05";

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public BillService(BillRepository billRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public BillResponse createAndSaveBill(BillRequest req) {
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + req.getCustomerId()));

        // 1) Calculate grocery & and non-grocery line items totals
        BigDecimal groceryTotal = BigDecimal.ZERO;
        BigDecimal nonGroceryTotal = BigDecimal.ZERO;

        for (BillLineRequest line : req.getLines()) {

            // Using something like a builder pattern chain of methods to return
            // line total = line item price times line item quantity
            BigDecimal lineTotal = line.getPrice()
                    .multiply(BigDecimal.valueOf(line.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            // just tracking the total for lines (line-totals=price*qty) based on item
            // category
            if (line.getItmCategory() == ItemCategory.GROCERY) {
                // tally up grocery line totals
                groceryTotal = groceryTotal.add(lineTotal);
            } else {
                // otherwise, tally up non-grocery line totals
                nonGroceryTotal = nonGroceryTotal.add(lineTotal);
            }
        }

        BigDecimal grossTotal = groceryTotal.add(nonGroceryTotal);

        // 2) Apply percentage discount on NON-GROCERY
        CustomerType percentType = null;
        BigDecimal percentRate = BigDecimal.ZERO;

        if (customer.getCusType() == CustomerType.EMPLOYEE) {
            percentType = CustomerType.EMPLOYEE;
            percentRate = new BigDecimal(EMP_DISCOUNT_PERCENT);
        } else if (customer.getCusType() == CustomerType.AFFILIATE) {
            percentType = CustomerType.AFFILIATE;
            percentRate = new BigDecimal(AFF_DISCOUNT_PERCENT);
        } else if (customer.getCusType() == CustomerType.LOYAL) {
            percentType = CustomerType.LOYAL;
            percentRate = new BigDecimal(LOY_DISCOUNT_PERCENT);
        }

        BigDecimal percentDiscountAmount = nonGroceryTotal
                .multiply(percentRate)
                .setScale(2, RoundingMode.HALF_UP);

        // 3) Flat discount: $5 per $100 in gross
        BigDecimal flatDiscountAmount = grossTotal
                .divideToIntegralValue(new BigDecimal("100"))
                .multiply(new BigDecimal("5"))
                .setScale(2, RoundingMode.HALF_UP);

        // 4) Totals
        BigDecimal totalDiscount = percentDiscountAmount.add(flatDiscountAmount)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal netPayable = grossTotal.subtract(totalDiscount)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);

        // 5) Persist Bill entity. This use JPA to store new bill record in the database
        Bill bill = new Bill();
        bill.setCustomer(customer);
        bill.setGrossTotal(grossTotal);
        bill.setGroceryTotal(groceryTotal);
        bill.setNonGroceryTotal(nonGroceryTotal);

        // This is the CHECK-constrained column -> enum is safe
        bill.setPercentDiscountType(percentType);
        bill.setPercentDiscountAmount(percentDiscountAmount);

        bill.setFlatDiscountAmount(flatDiscountAmount);
        bill.setTotalDiscount(totalDiscount);
        bill.setNetPayable(netPayable);

        billRepository.save(bill);

        // 6) Return response DTO
        return new BillResponse(
                grossTotal,
                groceryTotal,
                nonGroceryTotal,
                (percentType == null ? "NONE" : percentType.name()),
                percentDiscountAmount,
                flatDiscountAmount,
                totalDiscount,
                netPayable);
    }

    // must wrap in BillResponse, otherwise Spring Boot will freak out
    // since we already used Request & Response DTO
    // we use map to convert Bill entity to BillResponse DTO object
    public List<BillResponse> getAllBills() {
        return this.billRepository.findAll()
                .stream()
                .map(b -> new BillResponse(
                        b.getGrossTotal(),
                        b.getGroceryTotal(),
                        b.getNonGroceryTotal(),
                        b.getPercentDiscountType() == null ? "NONE" : b.getPercentDiscountType().name(),
                        b.getPercentDiscountAmount(),
                        b.getFlatDiscountAmount(),
                        b.getTotalDiscount(),
                        b.getNetPayable()))
                .collect(Collectors.toList());
    }

    public BillResponse getBillById(Long id) {
        Bill b = billRepository.findById(id).orElse(null);

        if (b == null)
            return null;
        else {
            return new BillResponse(
                    b.getGrossTotal(),
                    b.getGroceryTotal(),
                    b.getNonGroceryTotal(),
                    b.getPercentDiscountType() == null ? "NONE" : b.getPercentDiscountType().name(),
                    b.getPercentDiscountAmount(),
                    b.getFlatDiscountAmount(),
                    b.getTotalDiscount(),
                    b.getNetPayable());
        }
    }
}
