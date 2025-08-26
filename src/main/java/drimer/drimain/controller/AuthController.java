package drimer.drimain.controller;

import drimer.drimain.api.dto.AuthRequest;
import drimer.drimain.api.dto.AuthResponse;
import drimer.drimain.api.dto.UserInfo;
import drimer.drimain.model.User;
import drimer.drimain.security.JwtService;
import drimer.drimain.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            User user = (User) userDetailsService.loadUserByUsername(request.getUsername());
            List<String> roles = user.getAuthorities()
                    .stream().map(a -> a.getAuthority()).toList();
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles);
            
            // Add department information if available
            if (user.getDzial() != null) {
                claims.put("deptId", user.getDzial().getId());
                claims.put("deptName", user.getDzial().getNazwa());
            }

            String token = jwtService.generate(user.getUsername(), claims);
            Instant expiresAt = Instant.now().plus(jwtService.getTtlMinutes(), ChronoUnit.MINUTES);

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setExpiresAt(expiresAt);
            response.setRoles(roles);
            if (user.getDzial() != null) {
                response.setDeptId(user.getDzial().getId());
                response.setDeptName(user.getDzial().getNazwa());
            }

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new org.springframework.security.access.AccessDeniedException("Bad credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new org.springframework.security.access.AccessDeniedException("No token provided");
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            var claims = jwtService.parseSigned(token).getPayload();
            
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");
            userInfo.setRoles(roles);
            
            if (claims.get("deptId") != null) {
                userInfo.setDepartmentId(((Number) claims.get("deptId")).longValue());
                userInfo.setDepartmentName((String) claims.get("deptName"));
            }
            
            return ResponseEntity.ok(userInfo);
        } catch (Exception ex) {
            throw new org.springframework.security.access.AccessDeniedException("Invalid token");
        }
    }
}