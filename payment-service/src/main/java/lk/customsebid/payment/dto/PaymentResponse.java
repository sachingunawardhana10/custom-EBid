package lk.customsebid.payment.dto;

import lk.customsebid.payment.entity.Payment;
import lk.customsebid.payment.entity.PaymentMethod;
import lk.customsebid.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// data we send back, so the entity is not exposed directly
public record PaymentResponse(
        UUID id,
        UUID auctionId,
        UUID winnerId,
        BigDecimal amount,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        String transactionReference,
        Instant createdAt,
        Instant updatedAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAuctionId(),
                payment.getWinnerId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionReference(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
