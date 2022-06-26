package com.unzer.payments.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Getter
    @Value("${http.payments.process}")
    private String paymentProcessUrl;

    @Getter
    @Value("${http.payments.cancel}")
    private String paymentCancelUrl;
}
