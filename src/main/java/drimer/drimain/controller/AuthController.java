package drimer.drimain.controller;

import drimer.drimain.api.dto.AuthResponse;
import drimer.drimain.security.JwtService;
import drimer.drimain.service.CustomUserDetailsService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            
            // Get full User entity for department information
            var user = userDetailsService.getUserByUsername(request.getUsername());
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", userDetails.getAuthorities()
                    .stream().map(a -> a.getAuthority()).toList());
            
            // Add department claims if user has a department
            if (user.getDzial() != null) {
                claims.put("deptId", user.getDzial().getId());
                claims.put("deptName", user.getDzial().getNazwa());
            }

            String token = jwtService.generate(userDetails.getUsername(), claims);

            // Create response with department information
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setExpiresAt(java.time.Instant.now().plusSeconds(60 * 60)); // TODO: get from config
            response.setRoles(userDetails.getAuthorities()
                    .stream().map(a -> a.getAuthority()).toList());
            if (user.getDzial() != null) {
                response.setDepartmentId(user.getDzial().getId());
                response.setDepartmentName(user.getDzial().getNazwa());
            }

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Bad credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("No token");
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            return ResponseEntity.ok(username);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @Data
    public static class AuthRequest {
        private String username;
        private String password;
    }
}