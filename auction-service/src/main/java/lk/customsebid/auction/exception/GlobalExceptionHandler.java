package lk.customsebid.auction.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle invalid item operations
    @ExceptionHandler(ItemOperationException.class)
    public ResponseEntity<Map<String, Object>> handleItemOperationException(
            ItemOperationException exception,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", HttpStatus.CONFLICT.getReasonPhrase(),
                "message", exception.getMessage(),
                "path", request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }
}
