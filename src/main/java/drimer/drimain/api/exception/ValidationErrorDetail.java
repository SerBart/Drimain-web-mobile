package drimer.drimain.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorDetail {
    private String field;
    private String message;
}