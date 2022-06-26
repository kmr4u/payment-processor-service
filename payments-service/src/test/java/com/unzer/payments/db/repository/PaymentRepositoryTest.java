package com.unzer.payments.db.repository;

import com.unzer.payments.db.entity.Payment;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;


@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PaymentRepositoryTest {


    @ClassRule
    public static MySQLContainer container = new MySQLContainer()
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test");

    @Autowired
    private PaymentsRepository repository;

    private Payment payment1 = new Payment();
    private Payment payment2 = new Payment();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Before
    public void prepareData() {
        payment1.setId("1234");
        payment1.setCardNumber("1234-5678");
        payment1.setCardExpiryDate("12/2030");
        payment1.setCardCvc("123");
        payment1.setAmount("1000");
        payment1.setCurrency("USD");
        payment1.setApprovalCode("approved");


        payment2.setId("5678");
        payment2.setCardNumber("4321-8765");
        payment2.setCardExpiryDate("12/2030");
        payment2.setCardCvc("123");
        payment2.setAmount("1000");
        payment2.setCurrency("EUR");
        payment2.setApprovalCode("approved");

        List<Payment> payments = List.of(payment1, payment2);

        repository.saveAll(payments);
    }

    @Test
    @DisplayName("test create new payment with id")
    public void testCreatePayment() {

        Payment payment = new Payment();
        payment.setId("9999");
        payment.setCardNumber("9999-0000");
        payment.setCardExpiryDate("12/2030");
        payment.setCardCvc("786");
        payment.setAmount("1000");
        payment.setCurrency("INR");
        payment.setApprovalCode("approved");

        repository.save(payment);

        assertEquals(payment.getId(), repository.findById("9999").orElse(new Payment()).getId());
    }

    @Test
    @DisplayName("test retrieve existing payment with id")
    public void testRetrievePaymentById() {

        Payment result = repository.findById("5678").orElse(new Payment());

        assertEquals(result.getId(), payment2.getId());

    }

    @Test
    @DisplayName("test delete existing payment with id")
    public void testDeletePaymentById() {
        repository.deleteById("5678");

        Optional<Payment> result = repository.findById("5678");

        assertTrue(result.isEmpty());
    }

}
