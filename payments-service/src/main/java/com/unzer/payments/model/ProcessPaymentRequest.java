package com.unzer.payments.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessPaymentRequest {
    private String cardNumber;
    private String cardExpiryDate;
    private String cardCvc;
    private String amount;
    private String currency;
}
