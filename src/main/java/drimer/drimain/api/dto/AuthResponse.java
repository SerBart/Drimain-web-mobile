package drimer.drimain.api.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private Instant expiresAt;
    private List<String> roles;
    private Long deptId;
    private String deptName;
}