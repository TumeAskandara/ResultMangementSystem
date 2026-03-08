package com.example.resultmanagementsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - authentication
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/teachers/auth/**").permitAll()

                        // Swagger/OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()

                        // Actuator health
                        .requestMatchers("/actuator/health").permitAll()

                        // Public read access (GET only)
                        .requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()

                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "HOD")

                        // HR endpoints
                        .requestMatchers("/api/staff/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "HR_MANAGER")
                        .requestMatchers("/api/payroll/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "HR_MANAGER", "ACCOUNTANT")
                        .requestMatchers("/api/leave/**").authenticated()

                        // Finance endpoints
                        .requestMatchers("/api/fees/create", "/api/fees/generate").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "ACCOUNTANT")
                        .requestMatchers("/api/fee-categories/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "ACCOUNTANT")

                        // Library endpoints
                        .requestMatchers("/api/library/admin/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/library/**").authenticated()

                        // Transport endpoints
                        .requestMatchers("/api/transport/admin/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "TRANSPORT_MANAGER")

                        // Hostel endpoints
                        .requestMatchers("/api/hostel/admin/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN", "HOSTEL_WARDEN")

                        // Parent portal
                        .requestMatchers("/api/parent/**").hasAnyAuthority("PARENT", "ADMIN", "SUPER_ADMIN")

                        // All other endpoints require authentication
                        .requestMatchers("/api/results/**").authenticated()
                        .requestMatchers("/api/students/**").authenticated()
                        .requestMatchers("/api/teachers/**").authenticated()
                        .requestMatchers("/api/complaints/**").authenticated()
                        .requestMatchers("/api/fees/**").authenticated()
                        .requestMatchers("/api/assignments/**").authenticated()
                        .requestMatchers("/api/submissions/**").authenticated()
                        .requestMatchers("/api/calendar/**").authenticated()
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/api/timetables/**").authenticated()
                        .requestMatchers("/api/dashboard/**").authenticated()
                        .requestMatchers("/api/admissions/**").authenticated()
                        .requestMatchers("/api/attendance/**").authenticated()
                        .requestMatchers("/api/exams/**").authenticated()
                        .requestMatchers("/api/report-cards/**").authenticated()
                        .requestMatchers("/api/gradebook/**").authenticated()
                        .requestMatchers("/api/content/**").authenticated()
                        .requestMatchers("/api/discussions/**").authenticated()
                        .requestMatchers("/api/learning-paths/**").authenticated()
                        .requestMatchers("/api/chat/**").authenticated()
                        .requestMatchers("/api/classes/**").authenticated()
                        .requestMatchers("/api/analytics/**").authenticated()
                        .requestMatchers("/api/audit/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/announcements/**").authenticated()
                        .requestMatchers("/api/inventory/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
