package com.unzer.payments.db.dao;

import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.db.repository.PaymentsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentsDaoTest {

    @InjectMocks
    private PaymentsDao dao;

    @Mock
    private PaymentsRepository repository;

    Payment payment = null;

    @Before
    public void setup() {
        payment = new Payment();
        payment.setId("123");
        payment.setCardNumber("1234-5678");
        payment.setCardCvc("xyz");
        payment.setCardExpiryDate("12/2030");
        payment.setAmount("1000");
        payment.setCurrency("INR");
        payment.setApprovalCode("approved");
    }

    @Test
    @DisplayName("test create payment with an id should be successful")
    public void testCreatePayment() {

        when(repository.save(Mockito.any(Payment.class))).thenReturn(payment);

        String result = dao.createPayment(payment);

        assertEquals(result, payment.getId());
    }

    @Test
    @DisplayName("test retrieve existing payment with an id should be successful")
    public void testGetPaymentById() {

        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(payment));

        Payment result = dao.getPaymentById(payment.getId());

        assertEquals(result.getId(), payment.getId());

    }

    @Test
    @DisplayName("test deleting an existing payment with an id should be successful")
    public void testDeletePaymentById() {

        doNothing().when(repository).deleteById(Mockito.anyString());

        dao.deletePaymentById(payment.getId());

        verify(repository).deleteById(payment.getId());

    }

}
