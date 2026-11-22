package lk.customsebid.payment.entity;

// All the states a payment can be in
public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED
}
