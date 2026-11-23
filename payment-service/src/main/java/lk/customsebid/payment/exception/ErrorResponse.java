package lk.customsebid.payment.exception;

import java.time.Instant;

// common error format used by the team
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
