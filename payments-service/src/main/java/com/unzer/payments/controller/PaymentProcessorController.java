package com.unzer.payments.controller;

import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.exception.PaymentServiceExceptionHandler;
import com.unzer.payments.model.CreatePaymentRequest;
import com.unzer.payments.model.CreatePaymentResponse;
import com.unzer.payments.service.PaymentProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payments/{paymentId}")
public class PaymentProcessorController {

    @Autowired
    private PaymentProcessService paymentProcessService;

    @Operation(summary = "Create payment identified by given Id to retrieve an approval code")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Created payment with paymentId and approval code", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreatePaymentResponse.class))}),
    @ApiResponse(responseCode = "404", description = "Payment Id not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))}),
    @ApiResponse(responseCode = "400", description = "invalid approval code", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))}),
    @ApiResponse(responseCode = "502", description = "External Payment Processor Service not available", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))})})
    @RequestMapping(value= "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePaymentResponse> createPayment(@NotNull @PathVariable String paymentId, @RequestBody CreatePaymentRequest createPaymentRequest, @RequestHeader Map<String, String> headers) {

        CreatePaymentResponse response = paymentProcessService.createPayment(paymentId, createPaymentRequest);
        return new ResponseEntity<CreatePaymentResponse>(response, HttpStatus.OK);
    }

    @Operation(summary = "search payment identified by given Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Payment with details", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class))}),
    @ApiResponse(responseCode = "404", description = "Payment Id not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))}),
    @ApiResponse(responseCode = "502", description = "External Payment Processor Service not available", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))})})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> getPaymentById(@NotNull @PathVariable String paymentId, @RequestHeader Map<String, String> headers) {

        Payment response = paymentProcessService.searchPaymentById(paymentId);
        return new ResponseEntity<Payment>(response, HttpStatus.OK);
    }

    @Operation(summary = "cancel payment identified by given Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json")}),
    @ApiResponse(responseCode = "404", description = "Payment Id not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))}),
    @ApiResponse(responseCode = "502", description = "External Payment Processor Service not available", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentServiceExceptionHandler.ServiceError.class))})})
    @RequestMapping(value= "/cancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelPayment(@NotNull @PathVariable String paymentId, @RequestHeader Map<String, String> headers) {

        paymentProcessService.cancelPaymentById(paymentId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
