package lk.customsebid.payment.service;

import lk.customsebid.payment.dto.CreatePaymentRequest;
import lk.customsebid.payment.dto.PaymentResponse;
import lk.customsebid.payment.entity.Payment;
import lk.customsebid.payment.entity.PaymentMethod;
import lk.customsebid.payment.entity.PaymentStatus;
import lk.customsebid.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setAuctionId(request.auctionId());
        payment.setWinnerId(request.winnerId());
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.PENDING);

        // use MOCK when no method is given
        if (request.paymentMethod() != null) {
            payment.setPaymentMethod(request.paymentMethod());
        } else {
            payment.setPaymentMethod(PaymentMethod.MOCK);
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }
}
