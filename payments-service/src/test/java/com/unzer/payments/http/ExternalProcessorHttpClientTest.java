package com.unzer.payments.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payments.config.ApplicationConfig;
import com.unzer.payments.model.ProcessPaymentRequest;
import com.unzer.payments.model.ProcessPaymentResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ExternalProcessorHttpClientTest {

    @InjectMocks
    private ExternalProcessorHttpClient httpClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationConfig config;

    private ObjectMapper mapper = new ObjectMapper();
    @Before
    public void setup() {
        MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("test process payment should return an approval code")
    public void testProcessPaymentReturnsApprovalCode() {

        ProcessPaymentRequest req = ProcessPaymentRequest.builder()
                .cardNumber("1234")
                .cardCvc("123")
                .cardExpiryDate("12/2030")
                .currency("100")
                .amount("INR").build();

        ProcessPaymentResponse resp = new ProcessPaymentResponse();
        resp.setApprovalCode("approved");

        ResponseEntity<ProcessPaymentResponse> myEntity = new ResponseEntity<ProcessPaymentResponse>(resp, new HttpHeaders(), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<ProcessPaymentResponse>>any())
        ).thenReturn(myEntity);

        when(config.getPaymentProcessUrl()).thenReturn("http://localhost:8500/payments/process");

        String approvalCode = httpClient.processPayment(req);

        assertEquals(approvalCode, resp.getApprovalCode());
    }

    @Test
    @DisplayName("test cancel payment by approval code should return success")
    public void testCancelPaymentByApprovalCode() {

        ResponseEntity<String> myEntity = new ResponseEntity<String>("OK", new HttpHeaders(), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(myEntity);

        when(config.getPaymentCancelUrl()).thenReturn("http://localhost:8500/payments/{approvalCode}/cancel");

        String response = httpClient.cancelPayment("approved");

        assertEquals(response, "OK");
    }

}
