package com.unzer.payments.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class PaymentServiceExceptionHandler {
    @ExceptionHandler({ProcessPaymentException.class})
    public final ResponseEntity<ServiceError> handleProductCheckoutException(ProcessPaymentException ex) {
        ServiceError serviceError = ServiceError.from(ex);
        switch (ex.getErrorCode()) {
            case PAYMENT_ID_NOT_FOUND_EXCEPTION:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceError);
            case PAYMENT_PROCESS_ERROR:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceError);
            case PAYMENT_PROCESS_SERVICE_UNAVAILABLE:
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(serviceError);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serviceError);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceError> handleUnexpected(Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ServiceError(exception.getMessage(), null, null));
    }

    @Data
    public static class ServiceError {
        @NotNull
        private final String errorMessage;
        private final String errorCode;
        private final Object details;

        public static ServiceError from(ProcessPaymentException processPaymentException) {
            return new ServiceError(processPaymentException.getMessage(), processPaymentException.getErrorCode().name(), processPaymentException.getDetails());
        }
    }
}
