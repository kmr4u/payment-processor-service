package com.unzer.payments.model;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String cardNumber;
    private String cardExpiryDate;
    private String cardCvc;
    private String amount;
    private String currency;
}
