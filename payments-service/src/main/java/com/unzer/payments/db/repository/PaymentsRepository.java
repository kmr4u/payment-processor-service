package com.unzer.payments.db.repository;

import com.unzer.payments.db.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends CrudRepository<Payment, String> {
}
