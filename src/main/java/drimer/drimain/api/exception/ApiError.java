package drimer.drimain.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;
    private List<ValidationErrorDetail> details;
}