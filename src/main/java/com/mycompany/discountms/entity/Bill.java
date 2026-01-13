package com.mycompany.discountms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Yaqoub Alshatti
 */
@Entity
@Table(name = "bills", schema = "discount")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bll_id")
    private Long bllId;

    // FK: bills.cus_id -> customers.cus_id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cus_id", nullable = false)
    private Customer customer;

    @Column(name = "bll_original_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllOriginalAmount;

    @Column(name = "bll_grocery_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllGroceryAmount = BigDecimal.ZERO;

    @Column(name = "bll_non_grocery_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllNonGroceryAmount = BigDecimal.ZERO;

    @Column(name = "bll_percentage_discount_type", length = 50)
    private String bllPercentageDiscountType;

    @Column(name = "bll_percentage_discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllPercentageDiscountAmount = BigDecimal.ZERO;

    @Column(name = "bll_flat_discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllFlatDiscountAmount = BigDecimal.ZERO;

    @Column(name = "bll_total_discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllTotalDiscount = BigDecimal.ZERO;

    @Column(name = "bll_net_payable", nullable = false, precision = 12, scale = 2)
    private BigDecimal bllNetPayable;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Bill() {
    }

    public Bill(Customer customer,
                BigDecimal bllOriginalAmount,
                BigDecimal bllGroceryAmount,
                BigDecimal bllNonGroceryAmount,
                String bllPercentageDiscountType,
                BigDecimal bllPercentageDiscountAmount,
                BigDecimal bllFlatDiscountAmount,
                BigDecimal bllTotalDiscount,
                BigDecimal bllNetPayable) {
        this.customer = customer;
        this.bllOriginalAmount = bllOriginalAmount;
        this.bllGroceryAmount = bllGroceryAmount;
        this.bllNonGroceryAmount = bllNonGroceryAmount;
        this.bllPercentageDiscountType = bllPercentageDiscountType;
        this.bllPercentageDiscountAmount = bllPercentageDiscountAmount;
        this.bllFlatDiscountAmount = bllFlatDiscountAmount;
        this.bllTotalDiscount = bllTotalDiscount;
        this.bllNetPayable = bllNetPayable;
    }

    /* ===== Getters & Setters ===== */

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

    public BigDecimal getBllOriginalAmount() {
        return bllOriginalAmount;
    }

    public void setBllOriginalAmount(BigDecimal bllOriginalAmount) {
        this.bllOriginalAmount = bllOriginalAmount;
    }

    public BigDecimal getBllGroceryAmount() {
        return bllGroceryAmount;
    }

    public void setBllGroceryAmount(BigDecimal bllGroceryAmount) {
        this.bllGroceryAmount = bllGroceryAmount;
    }

    public BigDecimal getBllNonGroceryAmount() {
        return bllNonGroceryAmount;
    }

    public void setBllNonGroceryAmount(BigDecimal bllNonGroceryAmount) {
        this.bllNonGroceryAmount = bllNonGroceryAmount;
    }

    public String getBllPercentageDiscountType() {
        return bllPercentageDiscountType;
    }

    public void setBllPercentageDiscountType(String bllPercentageDiscountType) {
        this.bllPercentageDiscountType = bllPercentageDiscountType;
    }

    public BigDecimal getBllPercentageDiscountAmount() {
        return bllPercentageDiscountAmount;
    }

    public void setBllPercentageDiscountAmount(BigDecimal bllPercentageDiscountAmount) {
        this.bllPercentageDiscountAmount = bllPercentageDiscountAmount;
    }

    public BigDecimal getBllFlatDiscountAmount() {
        return bllFlatDiscountAmount;
    }

    public void setBllFlatDiscountAmount(BigDecimal bllFlatDiscountAmount) {
        this.bllFlatDiscountAmount = bllFlatDiscountAmount;
    }

    public BigDecimal getBllTotalDiscount() {
        return bllTotalDiscount;
    }

    public void setBllTotalDiscount(BigDecimal bllTotalDiscount) {
        this.bllTotalDiscount = bllTotalDiscount;
    }

    public BigDecimal getBllNetPayable() {
        return bllNetPayable;
    }

    public void setBllNetPayable(BigDecimal bllNetPayable) {
        this.bllNetPayable = bllNetPayable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
