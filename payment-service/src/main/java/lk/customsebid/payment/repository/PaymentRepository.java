package lk.customsebid.payment.repository;

import lk.customsebid.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
