package drimer.drimain.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that provides proper HTTP status codes for common errors.
 * This handler specifically addresses method not supported errors that were previously
 * returned as 500 Internal Server Error.
 * 
 * Only applies to REST API endpoints (paths starting with /api) to avoid interfering
 * with template-based pages that have their own error handling.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        
        // Only handle API requests, let web pages use default error handling
        if (request.getRequestURI().startsWith("/api")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "METHOD_NOT_ALLOWED");
            errorResponse.put("message", ex.getMessage());
            errorResponse.put("timestamp", Instant.now());
            errorResponse.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
            errorResponse.put("path", request.getRequestURI());
            
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
        }
        
        // For non-API requests, return null to let Spring's default handling take over
        return null;
    }
}