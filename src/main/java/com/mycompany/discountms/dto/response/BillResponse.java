package com.mycompany.discountms.dto.response;

import java.math.BigDecimal;

/**
 *
 * @author Yaqoub Alshatti
 */
public class BillResponse {

    private BigDecimal originalAmount; // using BIgDecimals with Numeric in Postgres for maximum precision
    private BigDecimal groceryAmount;
    private BigDecimal nonGroceryAmount;

    private String appliedPercentageDiscount;
    private BigDecimal percentageDiscountAmount;

    private BigDecimal flatDiscountAmount;

    private BigDecimal totalDiscount;
    private BigDecimal netPayableAmount;

    public BillResponse() {
    }

    public BillResponse(
            BigDecimal originalAmount,
            BigDecimal groceryAmount,
            BigDecimal nonGroceryAmount,
            String appliedPercentageDiscount,
            BigDecimal percentageDiscountAmount,
            BigDecimal flatDiscountAmount,
            BigDecimal totalDiscount,
            BigDecimal netPayableAmount) {

        this.originalAmount = originalAmount;
        this.groceryAmount = groceryAmount;
        this.nonGroceryAmount = nonGroceryAmount;
        this.appliedPercentageDiscount = appliedPercentageDiscount;
        this.percentageDiscountAmount = percentageDiscountAmount;
        this.flatDiscountAmount = flatDiscountAmount;
        this.totalDiscount = totalDiscount;
        this.netPayableAmount = netPayableAmount;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getGroceryAmount() {
        return groceryAmount;
    }

    public void setGroceryAmount(BigDecimal groceryAmount) {
        this.groceryAmount = groceryAmount;
    }

    public BigDecimal getNonGroceryAmount() {
        return nonGroceryAmount;
    }

    public void setNonGroceryAmount(BigDecimal nonGroceryAmount) {
        this.nonGroceryAmount = nonGroceryAmount;
    }

    public String getAppliedPercentageDiscount() {
        return appliedPercentageDiscount;
    }

    public void setAppliedPercentageDiscount(String appliedPercentageDiscount) {
        this.appliedPercentageDiscount = appliedPercentageDiscount;
    }

    public BigDecimal getPercentageDiscountAmount() {
        return percentageDiscountAmount;
    }

    public void setPercentageDiscountAmount(BigDecimal percentageDiscountAmount) {
        this.percentageDiscountAmount = percentageDiscountAmount;
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

    public BigDecimal getNetPayableAmount() {
        return netPayableAmount;
    }

    public void setNetPayableAmount(BigDecimal netPayableAmount) {
        this.netPayableAmount = netPayableAmount;
    }
}
