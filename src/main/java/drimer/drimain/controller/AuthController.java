package drimer.drimain.controller;

import drimer.drimain.api.dto.AuthRequest;
import drimer.drimain.api.dto.AuthResponse;
import drimer.drimain.api.dto.UserInfo;
import drimer.drimain.security.JwtService;
import drimer.drimain.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
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
            List<String> roles = userDetails.getAuthorities()
                    .stream().map(a -> a.getAuthority()).toList();
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles);

            String token = jwtService.generate(userDetails.getUsername(), claims);
            Instant expiresAt = Instant.now().plus(jwtService.getTtlMinutes(), ChronoUnit.MINUTES);

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setExpiresAt(expiresAt);
            response.setRoles(roles);

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
            Claims claims = jwtService.parseSigned(token).getPayload();
            String username = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");
            
            UserInfo userInfo = new UserInfo(username, roles != null ? roles : List.of());
            return ResponseEntity.ok(userInfo);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}