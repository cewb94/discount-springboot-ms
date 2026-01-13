package com.mycompany.discountms.entity;


import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.UpdateTimestamp;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;

// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.Mockito.*;


import com.mycompany.discountms.enums.CustomerType;

import jakarta.persistence.*;


/**
 *
 * @author Yaqoub Alshatti
 */
@Entity
@Table(name = "customers", schema = "discount")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_id")
    private Long cusId;

    @Column(name = "cus_full_name", nullable = false)
    private String cusFullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "cus_type", nullable = false)
    private CustomerType cusType;

    @Column(name = "cus_registration_date", nullable = false)
    private LocalDate cusRegistrationDate;

    @Column(name = "cus_blacklisted", nullable = false)
    private Boolean cusBlacklisted;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @UpdateTimestamp

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Customer() {
    }

    public Customer(String cusFullName, CustomerType cusType, LocalDate cusRegistrationDate, Boolean cusBlacklisted) {
        this.cusFullName = cusFullName;
        this.cusType = cusType;
        this.cusRegistrationDate = cusRegistrationDate;
        this.cusBlacklisted = cusBlacklisted;
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getCusId() {
        return cusId;
    }

    public void setCusId(Long cusId) {
        this.cusId = cusId;
    }

    public String getCusFullName() {
        return cusFullName;
    }

    public void setCusFullName(String cusFullName) {
        this.cusFullName = cusFullName;
    }

    public CustomerType getCusType() {
        return cusType;
    }

    public void setCusType(CustomerType cusType) {
        this.cusType = cusType;
    }

    public LocalDate getCusRegistrationDate() {
        return cusRegistrationDate;
    }

    public void setCusRegistrationDate(LocalDate cusRegistrationDate) {
        this.cusRegistrationDate = cusRegistrationDate;
    }

    public Boolean getCusBlacklisted() {
        return cusBlacklisted;
    }

    public void setCusBlacklisted(Boolean cusBlacklisted) {
        this.cusBlacklisted = cusBlacklisted;
    }

}
