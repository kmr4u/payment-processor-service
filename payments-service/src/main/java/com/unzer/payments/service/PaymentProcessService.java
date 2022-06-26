package com.unzer.payments.service;

import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.model.CreatePaymentRequest;
import com.unzer.payments.model.CreatePaymentResponse;

public interface PaymentProcessService {

    public CreatePaymentResponse createPayment(String paymentId, CreatePaymentRequest request);

    public Payment searchPaymentById(String paymentId);

    public void cancelPaymentById(String paymentId);
}
