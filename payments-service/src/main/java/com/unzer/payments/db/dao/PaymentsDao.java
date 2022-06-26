package com.unzer.payments.db.dao;

import com.unzer.payments.db.entity.Payment;
import com.unzer.payments.db.repository.PaymentsRepository;
import com.unzer.payments.exception.ProcessPaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PaymentsDao {

    @Autowired
    private PaymentsRepository repository;

    public Payment getPaymentById(String id) {
        log.info("searching for payment id {} in DB", id);
        try {
         Optional<Payment> paymentOptional = repository.findById(id) ;
         if(paymentOptional.isPresent()) return  paymentOptional.get();
         else throw new ProcessPaymentException(ProcessPaymentException.ErrorCode.PAYMENT_ID_NOT_FOUND_EXCEPTION, "Invalid Payment Id",  "Payment Id not found");
        } catch (Exception e) {
            log.error("Error while fetching payment from DB");
            throw e;
        }
    }

    public String createPayment(Payment payment) {
        log.info("saving payment in DB: {}", payment.getId());
        try {
          Payment savedPayment = repository.save(payment);
          return savedPayment.getId();
        } catch (Exception e) {
            log.error("Error while saving payment");
            throw e;
        }
    }

    public void deletePaymentById(String id) {
        log.info("deleting payment from DB: {}", id);
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while saving payment");
            throw e;
        }

    }
}
