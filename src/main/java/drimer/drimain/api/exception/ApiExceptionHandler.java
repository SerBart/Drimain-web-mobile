package drimer.drimain.api.exception;

import drimer.drimain.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegal(IllegalArgumentException ex) {
        ErrorResponse resp = new ErrorResponse(
                "IllegalArgument",
                ex.getMessage(),
                Instant.now(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse resp = new ErrorResponse(
                "AccessDenied",
                ex.getMessage(),
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(org.springframework.security.core.AuthenticationException ex) {
        ErrorResponse resp = new ErrorResponse(
                "AuthenticationFailed",
                ex.getMessage(),
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ErrorResponse resp = new ErrorResponse(
                "InternalError",
                ex.getMessage(),
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}