package com.stylish.stylish.filter;

import com.stylish.stylish.model.Role;
import com.stylish.stylish.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter logs all incoming requested endpoints for development purposes
 * it can certainly be modified to log request body, etc.
*/

@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();

        // Retrieve user details from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        Role role = Role.USER;

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            username = user.getUsername();
            role = user.getRole();
        }

        log.info("Incoming Request: {} {} {} - User: {} - Role: {}", method, uri, params != null ? "?" + params : "", username, role);
        filterChain.doFilter(request, response);
    }
}