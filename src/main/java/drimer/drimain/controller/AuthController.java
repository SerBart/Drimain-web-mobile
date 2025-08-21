package drimer.drimain.api.controller;

import drimer.drimain.api.dto.AuthRequest;
import drimer.drimain.api.dto.AuthResponse;
import drimer.drimain.security.JwtService;
import drimer.drimain.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails ud = (UserDetails) auth.getPrincipal();
        var roles = ud.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        String token = jwtService.generate(ud.getUsername(), Map.of("roles", roles));
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setRoles(roles);
        // Przybliżona data wygaśnięcia (dla uproszczenia +3600s)
        resp.setExpiresAt(Instant.now().plusSeconds(3600));
        return resp;
    }
}