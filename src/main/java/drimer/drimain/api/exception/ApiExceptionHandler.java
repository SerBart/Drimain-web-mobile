package drimer.drimain.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        
        ApiError error = new ApiError("VALIDATION_ERROR", "Validation failed", details);
        ApiErrorResponse resp = new ApiErrorResponse(
                Instant.now(),
                error
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resp);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegal(IllegalArgumentException ex) {
        ApiError error = new ApiError("INVALID_ARGUMENT", ex.getMessage(), List.of());
        ApiErrorResponse resp = new ApiErrorResponse(
                Instant.now(),
                error
        );
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiErrorResponse> handleSecurity(SecurityException ex) {
        ApiError error = new ApiError("ACCESS_DENIED", ex.getMessage(), List.of());
        ApiErrorResponse resp = new ApiErrorResponse(
                Instant.now(),
                error
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleOther(Exception ex) {
        ApiError error = new ApiError("INTERNAL_ERROR", ex.getMessage(), List.of());
        ApiErrorResponse resp = new ApiErrorResponse(
                Instant.now(),
                error
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}