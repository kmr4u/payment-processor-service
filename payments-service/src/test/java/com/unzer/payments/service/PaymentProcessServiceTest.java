package com.unzer.payments.service;

import com.unzer.payments.db.dao.PaymentsDao;
import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.http.ExternalProcessorHttpClient;
import com.unzer.payments.model.CreatePaymentRequest;
import com.unzer.payments.model.CreatePaymentResponse;
import com.unzer.payments.model.ProcessPaymentRequest;
import com.unzer.payments.service.impl.PaymentProcessServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentProcessServiceTest {

    @InjectMocks
    private PaymentProcessServiceImpl paymentProcessService;

    @Mock
    private PaymentsDao dao;

    @Mock
    private ExternalProcessorHttpClient httpClient;

    CreatePaymentRequest paymentReq = null;

    Payment payment = null;

    @Before
    public void setup() {
        paymentReq = new CreatePaymentRequest();
        paymentReq.setCardNumber("1234-5678");
        paymentReq.setCardCvc("xyz");
        paymentReq.setCardExpiryDate("12/2030");
        paymentReq.setAmount("1000");
        paymentReq.setCurrency("INR");

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
    @DisplayName("test create payment should return successful with payment id and approval code")
    public void testCreatePayment() {
        String paymentId = "123";
        String approvalCode = "approved";

        when(dao.createPayment(Mockito.any(Payment.class))).thenReturn(paymentId);
        when(httpClient.processPayment(Mockito.any(ProcessPaymentRequest.class))).thenReturn(approvalCode);

        CreatePaymentResponse response = paymentProcessService.createPayment(paymentId, paymentReq);

        assertEquals(response.getApprovalCode(), approvalCode);
        assertEquals(response.getPaymentId(), paymentId);
    }

    @Test
    @DisplayName("test create payment should return successful with payment id and approval code")
    public void testSearchPaymentById() {

        when(dao.getPaymentById(Mockito.anyString())).thenReturn(payment);

        Payment result = paymentProcessService.searchPaymentById("123");

        assertEquals(result.getId(), payment.getId());
        assertEquals(result.getApprovalCode(), payment.getApprovalCode());
    }

    @Test
    @DisplayName("test cancel payment by id should return successful")
    public void testCancelPayment() {
        when(dao.getPaymentById(Mockito.anyString())).thenReturn(payment);
        doNothing().when(dao).deletePaymentById(Mockito.anyString());
        when(httpClient.cancelPayment(Mockito.anyString())).thenReturn("Success");

        paymentProcessService.cancelPaymentById("123");

        verify(httpClient).cancelPayment(payment.getApprovalCode());
    }

}
