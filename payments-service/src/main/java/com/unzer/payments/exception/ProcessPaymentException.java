package com.unzer.payments.exception;

import lombok.Getter;

public class ProcessPaymentException extends RuntimeException{

    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final Object details;

    public ProcessPaymentException(ErrorCode errorCode, Object details, String message) {
        super(message);
        this.details = details;
        this.errorCode = errorCode;
    }

    public enum ErrorCode {
        PAYMENT_PROCESS_ERROR,
        PAYMENT_PROCESS_SERVICE_UNAVAILABLE,
        PAYMENT_ID_NOT_FOUND_EXCEPTION,
        PAYMENT_SERVICE_ERROR
    }
}
