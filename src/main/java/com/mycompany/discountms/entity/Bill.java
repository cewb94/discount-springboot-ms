package com.mycompany.discountms.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mycompany.discountms.enums.CustomerType;



/**
 * This entity holds the calculated amounts after applying the appropriate discounts
 * Bill entity still does not have line items and more reflects the respone JSON for simplicity
 * @author Yaqoub
 */
@Entity
@Table(name = "bills", schema = "discount")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bll_id")
    private Long bllId;

    // FK: discount.bills.cus_id -> discount.customers.cus_id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cus_id", nullable = false)
    private Customer customer;

    // The gross total of all item prices
    @Column(name = "bll_gross_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal grossTotal;

    // The total of grocery category item prices
    @Column(name = "bll_grocery_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal groceryTotal = BigDecimal.ZERO;

    // The total of non grocery prices
    @Column(name = "bll_non_grocery_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal nonGroceryTotal = BigDecimal.ZERO;


    // The type of percentage discount
    @Enumerated(EnumType.STRING)
    @Column(name = "bll_percent_discount_type", length = 50)
    private CustomerType percentDiscountType;


    // The calculated percentage discount deduction
    @Column(name = "bll_percent_discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal percentDiscountAmount = BigDecimal.ZERO;


    @Column(name = "bll_flat_discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal flatDiscountAmount = BigDecimal.ZERO;


    @Column(name = "bll_total_discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDiscount = BigDecimal.ZERO;


    @Column(name = "bll_net_payable", nullable = false, precision = 12, scale = 2)
    private BigDecimal netPayable;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Bill() {}

    public Bill(Customer customer,
                BigDecimal grossTotal,
                BigDecimal groceryTotal,
                BigDecimal nonGroceryTotal,
                CustomerType percentDiscountType,
                BigDecimal percentDiscountAmount,
                BigDecimal flatDiscountAmount,
                BigDecimal totalDiscount,
                BigDecimal netPayable) {
        this.customer = customer;
        this.grossTotal = grossTotal;
        this.groceryTotal = groceryTotal;
        this.nonGroceryTotal = nonGroceryTotal;
        this.percentDiscountType = percentDiscountType;
        this.percentDiscountAmount = percentDiscountAmount;
        this.flatDiscountAmount = flatDiscountAmount;
        this.totalDiscount = totalDiscount;
        this.netPayable = netPayable;
    }


    public Long getBllId() {
        return bllId;
    }

    public void setBllId(Long bllId) {
        this.bllId = bllId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(BigDecimal grossTotal) {
        this.grossTotal = grossTotal;
    }

    public BigDecimal getGroceryTotal() {
        return groceryTotal;
    }

    public void setGroceryTotal(BigDecimal groceryTotal) {
        this.groceryTotal = groceryTotal;
    }

    public BigDecimal getNonGroceryTotal() {
        return nonGroceryTotal;
    }

    public void setNonGroceryTotal(BigDecimal nonGroceryTotal) {
        this.nonGroceryTotal = nonGroceryTotal;
    }

    public CustomerType getPercentDiscountType() {
        return percentDiscountType;
    }

    public void setPercentDiscountType(CustomerType percentDiscountType) {
        this.percentDiscountType = percentDiscountType;
    }

    public BigDecimal getPercentDiscountAmount() {
        return percentDiscountAmount;
    }

    public void setPercentDiscountAmount(BigDecimal percentDiscountAmount) {
        this.percentDiscountAmount = percentDiscountAmount;
    }

    public BigDecimal getFlatDiscountAmount() {
        return flatDiscountAmount;
    }

    public void setFlatDiscountAmount(BigDecimal flatDiscountAmount) {
        this.flatDiscountAmount = flatDiscountAmount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(BigDecimal netPayable) {
        this.netPayable = netPayable;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
