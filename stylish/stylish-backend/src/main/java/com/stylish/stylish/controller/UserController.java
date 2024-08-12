package com.stylish.stylish.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylish.stylish.annotation.RateLimited;
import com.stylish.stylish.dto.CartItem;
import com.stylish.stylish.errors.ErrorResponse;
import com.stylish.stylish.model.*;
import com.stylish.stylish.security.AuthenticationResponse;
import com.stylish.stylish.security.AuthenticationService;
import com.stylish.stylish.security.JwtService;
import com.stylish.stylish.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.ObjectError;

import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/${apiVersion}/user")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Autowired
    public UserController(AuthenticationService authenticationService, UserService userService, ObjectMapper objectMapper, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid
                @RequestBody SignUpRequest request) {
        return ResponseEntity.ok().body(Map.of("data", authenticationService.registerUser(request)));
    }

    @RateLimited(limit = 1, windowMillis = 10000)
    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(signInRequest);
        log.info(authenticationResponse);
       	return ResponseEntity.status(HttpStatus.OK).body(Map.of("data",authenticationResponse));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            Map<String, String > profile  = new HashMap<>();
            profile.put("provider", user.getProvider());
            profile.put("name", user.getName());
            profile.put("email", user.getEmail());
            profile.put("picture", user.getPicture());

            return ResponseEntity.ok().body(Map.of("data", profile));
        }
        return  ResponseEntity.status(403).body("no valid authorization");
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok().body(Map.of("data", userService.getCartItems(user.getId())));
        }
        return  ResponseEntity.status(403).body("no valid authorization");
    }

    @PostMapping("/cart")
    public ResponseEntity<?> updateCart(@RequestBody List<CartItem> cart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userService.saveCartItems(user.getId(),cart);
            return ResponseEntity.ok().body(Map.of("data","success"));
        }
        return  ResponseEntity.status(403).body("no valid authorization");
    }

}
