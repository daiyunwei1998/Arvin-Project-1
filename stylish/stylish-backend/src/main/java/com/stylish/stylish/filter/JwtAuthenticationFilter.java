package com.stylish.stylish.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylish.stylish.model.User;
import com.stylish.stylish.security.JwtService;
import com.stylish.stylish.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final JwtService jwtService;
        private final UserService userService;
        @Value("${apiVersion}")
        private String apiVersion;
        public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
            this.jwtService = jwtService;
            this.userService = userService;
        }
        @Override
        protected void doFilterInternal(
                @NotNull HttpServletRequest request,
                @NotNull HttpServletResponse response,
 		@NotNull FilterChain filterChain
        ) throws ServletException, IOException {
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                SecurityContextHolder.clearContext();
                sendError("No JWT token", HttpServletResponse.SC_UNAUTHORIZED, response);
                return;
            }
            jwt = authHeader.substring(7);

            try {
                if (jwtService.isTokenExpired(jwt)) {
                    SecurityContextHolder.clearContext();
                    sendError("Expired JWT token", HttpServletResponse.SC_FORBIDDEN, response);
                    return;
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user =  jwtService.getUserFromToken(jwt);

                    if (jwtService.isTokenValid(jwt, user)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        // update authentication
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (SignatureException e) {
                // Let the exception propagate to be handled by the global exception handler
                sendError("Invalid JWT token", HttpServletResponse.SC_FORBIDDEN, response);

            }
}

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // Define public endpoints with allowed methods
        String[] publicGetEndpoints = {
                "/api/" + apiVersion + "/user/signup",
                "/api/" + apiVersion + "/user/signin",
                "/api/" + apiVersion + "/products/*",
                "/api/" + apiVersion + "/products/{id}/*",
                "/api/" + apiVersion + "/marketing/*",
                "/api/" + apiVersion + "/marketing/campaigns",
                "/api/1.0/products",
                "/api/1.0/report/payments",
                "/api/2.0/report/payments"
        };

        // Define endpoints that are public only for GET requests
        String[] publicGetOnlyEndpoints = {
                "/api/" + apiVersion + "/marketing/campaigns",
        };

        // Check if the request method is POST and the endpoint is in the restricted list
        if ("POST".equalsIgnoreCase(method)) {
            for (String endpoint : publicGetOnlyEndpoints) {
                if (pathMatcher.match(endpoint, path)) {
                    return false; // Restrict POST method for this endpoint
                }
            }
        }

        // Check if the path matches any public GET endpoints
        for (String endpoint : publicGetEndpoints) {
            if (pathMatcher.match(endpoint, path)) {
                return true;
            }
        }

        return false;
    }

    private static void sendError(String message, int status,  HttpServletResponse response) throws IOException {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorDetails);
        response.setStatus(status);
        response.getWriter().write(jsonResponse);
    }

}
