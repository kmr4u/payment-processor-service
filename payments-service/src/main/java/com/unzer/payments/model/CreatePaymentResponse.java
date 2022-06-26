package com.unzer.payments.model;

import lombok.Data;

@Data
public class CreatePaymentResponse {
    private String paymentId;
    private String approvalCode;
}
