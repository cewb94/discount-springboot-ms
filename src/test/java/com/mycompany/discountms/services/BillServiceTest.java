package com.mycompany.discountms.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.discountms.dto.request.BillLineRequest;
import com.mycompany.discountms.dto.request.BillRequest;
import com.mycompany.discountms.dto.response.BillResponse;
import com.mycompany.discountms.entity.Bill;
import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.enums.CustomerType;
import com.mycompany.discountms.enums.ItemCategory;
import com.mycompany.discountms.repository.BillRepository;
import com.mycompany.discountms.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock BillRepository billRepository;
    @Mock CustomerRepository customerRepository;

    @InjectMocks BillService billService;

    /*
     
     */
    @Test
    void createAndSaveBill_employee_applies30PercentOnNonGrocery_only() {
        // Arrange: customerId=1 is EMPLOYEE
        Customer c = new Customer("Test Emp", CustomerType.EMPLOYEE, LocalDate.now(), false);
        c.setCusId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        BillLineRequest grocery = new BillLineRequest();
        grocery.setItmId(1L);
        grocery.setItmCategory(ItemCategory.GROCERY);
        grocery.setPrice(new BigDecimal("2.00"));
        grocery.setQuantity(3); // 6.00

        BillLineRequest nongrocery = new BillLineRequest();
        nongrocery.setItmId(11L);
        nongrocery.setItmCategory(ItemCategory.NON_GROCERY);
        nongrocery.setPrice(new BigDecimal("10.00"));
        nongrocery.setQuantity(1); // 10.00

        BillRequest req = new BillRequest();
        req.setCustomerId(1L);
        req.setLines(List.of(grocery, nongrocery));

        // Capture what we save
        ArgumentCaptor<Bill> billCaptor = ArgumentCaptor.forClass(Bill.class);
        when(billRepository.save(billCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        BillResponse resp = billService.createAndSaveBill(req);

        // Assert response math
        assertThat(resp.getOriginalAmount()).isEqualByComparingTo("16.00");
        assertThat(resp.getGroceryAmount()).isEqualByComparingTo("6.00");
        assertThat(resp.getNonGroceryAmount()).isEqualByComparingTo("10.00");

        assertThat(resp.getAppliedPercentageDiscount()).isEqualTo("EMPLOYEE");
        assertThat(resp.getPercentageDiscountAmount()).isEqualByComparingTo("3.00"); // 30% of 10.00
        assertThat(resp.getFlatDiscountAmount()).isEqualByComparingTo("0.00");       // < 100 gross
        assertThat(resp.getTotalDiscount()).isEqualByComparingTo("3.00");
        assertThat(resp.getNetPayableAmount()).isEqualByComparingTo("13.00");

        // Assert what was persisted
        Bill saved = billCaptor.getValue();
        assertThat(saved.getCustomer()).isSameAs(c);
        assertThat(saved.getGrossTotal()).isEqualByComparingTo("16.00");
        assertThat(saved.getGroceryTotal()).isEqualByComparingTo("6.00");
        assertThat(saved.getNonGroceryTotal()).isEqualByComparingTo("10.00");
        assertThat(saved.getPercentDiscountType()).isEqualTo(CustomerType.EMPLOYEE);
        assertThat(saved.getPercentDiscountAmount()).isEqualByComparingTo("3.00");
        assertThat(saved.getFlatDiscountAmount()).isEqualByComparingTo("0.00");
        assertThat(saved.getTotalDiscount()).isEqualByComparingTo("3.00");
        assertThat(saved.getNetPayable()).isEqualByComparingTo("13.00");

        verify(customerRepository).findById(1L);
        verify(billRepository).save(any(Bill.class));
        verifyNoMoreInteractions(customerRepository, billRepository);
    }

    @Test
    void createAndSaveBill_customerNotFound_throws() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        BillRequest req = new BillRequest();
        req.setCustomerId(99L);
        req.setLines(List.of());

        assertThatThrownBy(() -> billService.createAndSaveBill(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer not found: 99");

        verify(customerRepository).findById(99L);
        verifyNoInteractions(billRepository);
    }

    @Test
    void getBillById_missing_returnsNull() {
        when(billRepository.findById(123L)).thenReturn(Optional.empty());

        BillResponse resp = billService.getBillById(123L);
        assertThat(resp).isNull();

        verify(billRepository).findById(123L);
    }
}
