package com.mycompany.discountms.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    BillRepository billRepository;
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    BillService billService;

    /*
    Expected BillResponse JSON:

    {
        "originalAmount": 16.00,
        "groceryAmount": 6.00,
        "nonGroceryAmount": 10.00,
        "appliedPercentageDiscount": "EMPLOYEE",
        "percentageDiscountAmount": 3.00,
        "flatDiscountAmount": 0.00,
        "totalDiscount": 3.00,
        "netPayableAmount": 13.00
    }

    Explanation:
        - Grocery: 2.00 × 3 = 6.00
        - Non-grocery: 10.00 × 1 = 10.00
        - Gross total = 16.00
        - EMPLOYEE → 30% off non-grocery = 3.00
        - Flat discount = 0.00 (gross < 100)
        - Net payable = 16.00 − 3.00 = 13.00
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
        assertThat(resp.getFlatDiscountAmount()).isEqualByComparingTo("0.00"); // < 100 gross
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

    /*
     * Cover getBillById(not found) mapping branch
     */
    @Test
    void getBillById_missing_returnsNull() {
        when(billRepository.findById(123L)).thenReturn(Optional.empty());

        BillResponse resp = billService.getBillById(123L);
        assertThat(resp).isNull();

        verify(billRepository).findById(123L);
    }

    /*
     * Cover LOYAL branch (5% off non-grocery)
     *
     * IMPORTANT: Service requires loyal registration date older than 2 years.
     */
    @Test
    void createAndSaveBill_loyal_applies5PercentOnNonGrocery_only() {
        // ✅ minimal fix: make customer eligible by being registered > 2 years ago
        Customer c = new Customer("Test Loyal", CustomerType.LOYAL, LocalDate.now().minusYears(3), false);
        c.setCusId(2L);

        when(customerRepository.findById(2L)).thenReturn(Optional.of(c));
        when(billRepository.save(any(Bill.class))).thenAnswer(inv -> inv.getArgument(0));

        BillLineRequest grocery = new BillLineRequest();
        grocery.setItmId(1L);
        grocery.setItmCategory(ItemCategory.GROCERY);
        grocery.setPrice(new BigDecimal("20.00"));
        grocery.setQuantity(1); // 20.00

        BillLineRequest nongrocery = new BillLineRequest();
        nongrocery.setItmId(11L);
        nongrocery.setItmCategory(ItemCategory.NON_GROCERY);
        nongrocery.setPrice(new BigDecimal("40.00"));
        nongrocery.setQuantity(1); // 40.00

        BillRequest req = new BillRequest();
        req.setCustomerId(2L);
        req.setLines(List.of(grocery, nongrocery));

        BillResponse resp = billService.createAndSaveBill(req);

        // Gross = 60.00, non-grocery = 40.00, LOYAL 5% => 2.00, flat = 0
        assertThat(resp.getOriginalAmount()).isEqualByComparingTo("60.00");
        assertThat(resp.getNonGroceryAmount()).isEqualByComparingTo("40.00");
        assertThat(resp.getAppliedPercentageDiscount()).isEqualTo("LOYAL");
        assertThat(resp.getPercentageDiscountAmount()).isEqualByComparingTo("2.00");
        assertThat(resp.getFlatDiscountAmount()).isEqualByComparingTo("0.00");
        assertThat(resp.getNetPayableAmount()).isEqualByComparingTo("58.00");
    }

    /*
     * Cover AFFILIATE branch (10%) + flat discount branch (>= 100)
     */
    @Test
    void createAndSaveBill_affiliate_applies10Percent_and_flatDiscount_whenOver100() {
        Customer c = new Customer("Test Affiliate", CustomerType.AFFILIATE, LocalDate.now(), false);
        c.setCusId(3L);

        when(customerRepository.findById(3L)).thenReturn(Optional.of(c));
        when(billRepository.save(any(Bill.class))).thenAnswer(inv -> inv.getArgument(0));

        // Make gross >= 100 to trigger flat discount
        BillLineRequest grocery = new BillLineRequest();
        grocery.setItmId(2L);
        grocery.setItmCategory(ItemCategory.GROCERY);
        grocery.setPrice(new BigDecimal("60.00"));
        grocery.setQuantity(1); // 60.00

        BillLineRequest nongrocery = new BillLineRequest();
        nongrocery.setItmId(12L);
        nongrocery.setItmCategory(ItemCategory.NON_GROCERY);
        nongrocery.setPrice(new BigDecimal("50.00"));
        nongrocery.setQuantity(1); // 50.00

        BillRequest req = new BillRequest();
        req.setCustomerId(3L);
        req.setLines(List.of(grocery, nongrocery));

        BillResponse resp = billService.createAndSaveBill(req);

        // Gross=110.00
        // Affiliate 10% on non-grocery 50 => 5.00
        // Flat discount: floor(110/100)=1 => 5.00
        // Total discount = 10.00, net = 100.00
        assertThat(resp.getOriginalAmount()).isEqualByComparingTo("110.00");
        assertThat(resp.getAppliedPercentageDiscount()).isEqualTo("AFFILIATE");
        assertThat(resp.getPercentageDiscountAmount()).isEqualByComparingTo("5.00");
        assertThat(resp.getFlatDiscountAmount()).isEqualByComparingTo("5.00");
        assertThat(resp.getTotalDiscount()).isEqualByComparingTo("10.00");
        assertThat(resp.getNetPayableAmount()).isEqualByComparingTo("100.00");
    }

    /*
     * Cover getBillById(found) mapping branch
     */
    @Test
    void getBillById_found_returnsMappedResponse() {
        Bill bill = new Bill();
        bill.setGrossTotal(new BigDecimal("16.00"));
        bill.setGroceryTotal(new BigDecimal("6.00"));
        bill.setNonGroceryTotal(new BigDecimal("10.00"));
        bill.setPercentDiscountType(CustomerType.EMPLOYEE);
        bill.setPercentDiscountAmount(new BigDecimal("3.00"));
        bill.setFlatDiscountAmount(new BigDecimal("0.00"));
        bill.setTotalDiscount(new BigDecimal("3.00"));
        bill.setNetPayable(new BigDecimal("13.00"));

        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));

        BillResponse resp = billService.getBillById(1L);

        assertThat(resp).isNotNull();
        assertThat(resp.getOriginalAmount()).isEqualByComparingTo("16.00");
        assertThat(resp.getAppliedPercentageDiscount()).isEqualTo("EMPLOYEE");
        assertThat(resp.getNetPayableAmount()).isEqualByComparingTo("13.00");

        verify(billRepository).findById(1L);
    }
}
