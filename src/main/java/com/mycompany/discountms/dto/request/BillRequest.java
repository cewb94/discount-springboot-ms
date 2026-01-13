package com.mycompany.discountms.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author Yaqoub Alshatti
 */
public class BillRequest {

    /*
     * No constructor needed because Jackson will just use getters and setters which i included in spring-boot-starter-web
     */

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotEmpty(message = "At least one bill line is required")
    private List<@Valid BillLineRequest> lines;

    public BillRequest() {
    }

    public BillRequest(Long customerId, List<BillLineRequest> lines) {
        this.customerId = customerId;
        this.lines = lines;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<BillLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<BillLineRequest> lines) {
        this.lines = lines;
    }
}
