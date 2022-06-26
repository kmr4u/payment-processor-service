package com.unzer.payments.http;

import com.unzer.payments.config.ApplicationConfig;
import com.unzer.payments.exception.ProcessPaymentException;
import com.unzer.payments.model.ProcessPaymentRequest;
import com.unzer.payments.model.ProcessPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.unzer.payments.exception.ProcessPaymentException.ErrorCode.*;

@Slf4j
@Component
public class ExternalProcessorHttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationConfig config;

    public String processPayment(ProcessPaymentRequest paymentRequest) {
        log.info("contacting external payment processor api for approval");
        HttpEntity<ProcessPaymentRequest> entity = new HttpEntity<>(paymentRequest, null);

        try{
            ResponseEntity<ProcessPaymentResponse> responseEntity = restTemplate.exchange(URI.create(config.getPaymentProcessUrl()), HttpMethod.POST, entity, ProcessPaymentResponse.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("payment approved");
                return responseEntity.getBody().getApprovalCode();
            }
        }catch(HttpStatusCodeException e) {
            log.error("Error while contacting external payment processor api");
            if(e.getStatusCode().is4xxClientError()){
                throw new ProcessPaymentException(PAYMENT_PROCESS_ERROR, e.getMessage(), "Error while processing payment");
            }else if(e.getStatusCode().is4xxClientError()){
                throw new ProcessPaymentException(PAYMENT_PROCESS_SERVICE_UNAVAILABLE, e.getMessage(), "payment process service unavailable");
            }
        }

        return null;
    }

    public String cancelPayment(String approvalCode) {
        log.info("contacting external payment processor api for cancellation");
        Map<String, Object> map = new HashMap<>();
        map.put("approvalCode", approvalCode);

        HttpEntity<?> entity = new HttpEntity<>(null);
        try{
            URI uri = UriComponentsBuilder.fromUriString(config.getPaymentCancelUrl()).uriVariables(map).build().toUri();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("payment cancellation succeeded");
                return responseEntity.getBody() != null ? responseEntity.getBody() : "Success";
            }
        }catch(HttpStatusCodeException e) {
            log.error("Error while contacting external payment processor api");
            if(e.getStatusCode().is4xxClientError()){
                throw new ProcessPaymentException(PAYMENT_PROCESS_ERROR, e.getMessage(), "Could not cancel payment");
            }else if(e.getStatusCode().is4xxClientError()){
                throw new ProcessPaymentException(PAYMENT_PROCESS_SERVICE_UNAVAILABLE, e.getMessage(), "payment process service unavailable");
            }
        }

        return null;
    }
}
