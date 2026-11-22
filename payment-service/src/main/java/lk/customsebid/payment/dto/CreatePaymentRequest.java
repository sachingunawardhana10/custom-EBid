package lk.customsebid.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lk.customsebid.payment.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

// data the client sends to create a payment
public record CreatePaymentRequest(

        @NotNull
        UUID auctionId,

        @NotNull
        UUID winnerId,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,

        // optional, MOCK is used if empty
        PaymentMethod paymentMethod

) {
}
