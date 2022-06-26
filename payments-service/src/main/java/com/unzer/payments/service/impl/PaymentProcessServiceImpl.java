package com.unzer.payments.service.impl;

import com.unzer.payments.db.dao.PaymentsDao;
import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.http.ExternalProcessorHttpClient;
import com.unzer.payments.model.CreatePaymentRequest;
import com.unzer.payments.model.CreatePaymentResponse;
import com.unzer.payments.model.ProcessPaymentRequest;
import com.unzer.payments.service.PaymentProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class PaymentProcessServiceImpl implements PaymentProcessService {

    @Autowired
    private ExternalProcessorHttpClient httpClient;
    @Autowired
    private PaymentsDao dao;

    @Override
    public CreatePaymentResponse createPayment(String paymentId, CreatePaymentRequest request) {
        log.info("create payment requested: {}", paymentId);

        ProcessPaymentRequest processRequest = ProcessPaymentRequest.builder()
                .cardNumber(request.getCardNumber())
                .cardCvc(request.getCardCvc())
                .cardExpiryDate(request.getCardExpiryDate())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .build();

        String approvalCode = httpClient.processPayment(processRequest);

        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setApprovalCode(approvalCode);
        payment.setCardNumber(request.getCardNumber());
        payment.setCardCvc(request.getCardCvc());
        payment.setCardExpiryDate(request.getCardExpiryDate());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());

        dao.createPayment(payment);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setPaymentId(paymentId);
        response.setApprovalCode(approvalCode);

        return response;
    }

    @Override
    public Payment searchPaymentById(String paymentId) {
        log.info("search payment requested: {}", paymentId);
        return dao.getPaymentById(paymentId);
    }

    @Override
    public void cancelPaymentById(String paymentId) {
        log.info("cancel payment requested: {}", paymentId);
        Payment payment = dao.getPaymentById(paymentId);
        String resp = httpClient.cancelPayment(payment.getApprovalCode());

        if(!StringUtils.isEmpty(resp)) {
            dao.deletePaymentById(paymentId);
        }
    }
}
