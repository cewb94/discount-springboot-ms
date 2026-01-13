package com.mycompany.discountms.dto.request;

import java.math.BigDecimal;

import com.mycompany.discountms.enums.ItemCategory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Yaqoub Alshatti
 */
public class BillRequest {

    /*
        No constructor needed because Jackson will just use getters and setters which i included in spring-boot-starter-web
     */

    @NotNull(message = "Item ID is required")
    private Long itmId;  // Reference existing item from DB

    @NotBlank(message = "Item name is optional if item id is provided")
    private String itmName;  // Optional if itmId is used, but validated if present

    @NotNull(message = "Item category is required")
    private ItemCategory itmCategory;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;


    public Long getItmId() {
        return itmId;
    }

    public void setItmId(Long itmId) {
        this.itmId = itmId;
    }

    public String getItmName() {
        return itmName;
    }

    public void setItmName(String itmName) {
        this.itmName = itmName;
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
