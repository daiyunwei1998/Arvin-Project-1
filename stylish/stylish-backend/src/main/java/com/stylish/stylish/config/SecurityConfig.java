package com.stylish.stylish.config;

import com.stylish.stylish.exception.JwtAuthenticationEntryPoint;
import com.stylish.stylish.filter.JwtAuthenticationFilter;
import com.stylish.stylish.filter.RequestLoggingFilter;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Value("${apiVersion}")
    private String apiVersion;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ApplicationConfig applicationConfig;
    private final RequestLoggingFilter requestLoggingFilter;
    @Qualifier("delegatedAuthenticationEntryPoint")
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ApplicationConfig applicationConfig, RequestLoggingFilter requestLoggingFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.applicationConfig = applicationConfig;
        this.requestLoggingFilter = requestLoggingFilter;

        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // basically csrf -> csrf.disable(), but with method reference
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/" + apiVersion + "/marketing/campaigns").hasRole("ADMIN")
                                .requestMatchers("/api/" + apiVersion + "/user/signup").permitAll()
                                .requestMatchers("/api/" + apiVersion + "/user/signin").permitAll()
                                .requestMatchers("/api/" + apiVersion + "/products/*").permitAll()
                                .requestMatchers("/api/" + apiVersion + "/products/{id}/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/" + apiVersion + "/marketing/campaigns").permitAll()
                                .requestMatchers("/api/" + apiVersion + "/order/checkout").permitAll()
                                .requestMatchers("/api/1.0/products").permitAll()
                                .requestMatchers( "/api/1.0/report/payments").permitAll()
                                .requestMatchers( "/api/2.0/report/payments").permitAll()
                                .anyRequest().authenticated()
                        )
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .authenticationProvider(applicationConfig.authenticationProvider()).exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(requestLoggingFilter, JwtAuthenticationFilter.class);

        return http.build();
    }


}