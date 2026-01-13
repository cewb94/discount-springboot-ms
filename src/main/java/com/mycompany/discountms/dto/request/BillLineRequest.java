package com.mycompany.discountms.dto.request;

import java.math.BigDecimal;

import com.mycompany.discountms.enums.ItemCategory;

import jakarta.validation.constraints.*;

/**
 * Adding this here for more realistic bill request with actual lines
 * This does not map to any entity class nor does it map to a table in the database
 * @author Yaqoub Alshatti
 */
public class BillLineRequest {

    @NotNull(message = "Item ID is required")
    private Long itmId;

    @NotNull(message = "Item category is required")
    private ItemCategory itmCategory;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // --- Getters & Setters ---

    public Long getItmId() {
        return itmId;
    }

    public void setItmId(Long itmId) {
        this.itmId = itmId;
    }

    public ItemCategory getItmCategory() {
        return itmCategory;
    }

    public void setItmCategory(ItemCategory itmCategory) {
        this.itmCategory = itmCategory;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
