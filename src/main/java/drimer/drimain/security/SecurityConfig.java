package drimer.drimain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)  // Enable @Secured annotations for future use
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF is disabled for simplicity - TODO: Consider enabling CSRF for web endpoints 
        // while excluding /api/** for better security. This is a trade-off for easier development.
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(reg -> reg
                        // Public resources and authentication endpoints
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", 
                                        "/webjars/**", "/favicon.ico", "/api/auth/**").permitAll()
                        // Admin area requires ROLE_ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Dashboard requires authentication 
                        .requestMatchers("/dashboard").authenticated()
                        // Consider protecting /zgloszenia/** if it should be limited - currently open
                        .requestMatchers("/zgloszenia/**").permitAll()  // TODO: Change to authenticated() if needed
                        // API endpoints require authentication (JWT will be handled by JwtAuthFilter)
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        // All other requests are open
                        .anyRequest().permitAll()
                )
                // Configure form-based login for web interface
                // Previously, POST /login was not supported because formLogin() was missing,
                // causing HttpRequestMethodNotSupportedException that appeared as 500 InternalError
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")  // This enables POST /login handling
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .httpBasic(Customizer.withDefaults());

        // Keep JWT authentication filter for /api/** endpoints
        // This allows both session-based auth (for web) and JWT auth (for API) to coexist
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(passwordEncoder());
        p.setUserDetailsService(userDetailsService);
        return new ProviderManager(p);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}