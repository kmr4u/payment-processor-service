package com.unzer.payments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payments.PaymentProcessorApplication;
import com.unzer.payments.model.CreatePaymentRequest;
import com.unzer.payments.model.ProcessPaymentRequest;
import com.unzer.payments.model.ProcessPaymentResponse;
import com.unzer.payments.service.PaymentProcessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentProcessorApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentProcessorControllerIntegrationTest {
    private final static String URL_PROCESS_PAYMENT_EXTERNAL = "http://localhost:8500/payments/process";
    private final static String URL_CANCEL_PAYMENT_EXTERNAL = "http://localhost:8500/payments/{approvalCode}/cancel";
    private final static String URL_CREATE_PAYMENT = "/api/v1/payments/{paymentId}/create";
    private final static String URL_CANCEL_PAYMENT = "/api/v1/payments/{paymentId}/cancel";
    private final static String URL_GET_PAYMENT = "/api/v1/payments/{paymentId}";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PaymentProcessService paymentProcessService;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init(){
        mockServer = MockRestServiceServer.createServer(this.restTemplate);
    }

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void test_create_payment() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setCardNumber("1234-5678");
        req.setCardCvc("xyz");
        req.setCardExpiryDate("12/2030");
        req.setAmount("1000");
        req.setCurrency("EUR");

        ProcessPaymentRequest
    }
    private void mockExternalProcessPaymentCall(ProcessPaymentRequest request) throws JsonProcessingException {
        ProcessPaymentResponse resp = new ProcessPaymentResponse();
        resp.setApprovalCode("abcd.efgh");
        mockServer.expect(ExpectedCount.once(), requestTo(URI.create(URL_PROCESS_PAYMENT_EXTERNAL)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(request)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(resp))
                );
    }

}
