package com.mycompany.discountms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mycompany.discountms.enums.ItemCategory;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author Yaqoub Alshatti
 */
@Entity
@Table(name = "items", schema = "discount")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itm_id")
    private Long itmId;

    @Column(name = "itm_name", nullable = false, length = 250)
    private String itmName;

    @Enumerated(EnumType.STRING)
    @Column(name = "itm_category", nullable = false, length = 20)
    private ItemCategory itmCategory;

    @Column(name = "itm_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal itmPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Item() {
    }

    public Item(String itmName, ItemCategory itmCategory, BigDecimal itmPrice) {
        this.itmName = itmName;
        this.itmCategory = itmCategory;
        this.itmPrice = itmPrice;
    }

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

    public BigDecimal getItmPrice() {
        return itmPrice;
    }

    public void setItmPrice(BigDecimal itmPrice) {
        this.itmPrice = itmPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
