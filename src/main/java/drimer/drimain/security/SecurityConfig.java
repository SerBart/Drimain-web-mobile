package drimer.drimain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/auth/**").permitAll()
                        // A3: ROLE_BIURO has full edit/delete rights (same as ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/zgloszenia/**").hasAnyRole("ADMIN", "BIURO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/zgloszenia/**").hasAnyRole("ADMIN", "BIURO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/zgloszenia/**").hasAnyRole("ADMIN", "BIURO", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/zgloszenia/**").hasAnyRole("ADMIN", "BIURO", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

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