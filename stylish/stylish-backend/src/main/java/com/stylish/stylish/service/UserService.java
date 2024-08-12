package com.stylish.stylish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylish.stylish.dto.CartItem;
import com.stylish.stylish.exception.UserNotFoundException;
import com.stylish.stylish.model.Role;
import com.stylish.stylish.model.User;
import com.stylish.stylish.security.JwtService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.stylish.stylish.repository.UserDAO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;


    @Autowired
    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder, JwtService jwtService, ObjectMapper objectMapper, RedisTemplate redisTemplate) {
        this.passwordEncoder = passwordEncoder;  // from security config
        this.userDAO = userDAO;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        // no user found
        return userDAO.getUserByUserName(username);
    }

    public User getUserDetailByEmail(String email) {
        // no user found
        return userDAO.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String registerUser(User user) throws DataAccessException {
        user.setPassword(encodePassword(user.getPassword()));
        userDAO.addUser(user);
        return jwtService.createLoginAccessToken(user);
    }

    public void saveCartItems(long userId, List<CartItem> cartItems) {
        redisTemplate.delete("cart:" + userId);
        try {
            for (CartItem cartItem : cartItems) {
                // Create a unique key for each cart item based on its ID
                String itemKey = "cartItem:" + cartItem.getId();
                // Store cart item as JSON
                redisTemplate.opsForHash().put("cart:" + userId, itemKey, objectMapper.writeValueAsString(cartItem));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle JSON processing exception
        }
    }

    public List<CartItem> getCartItems(long userId) {
        try {
            // Fetch all cart items for the user
            Map<Object, Object> entries = redisTemplate.opsForHash().entries("cart:" + userId);
            // Convert entries to List<CartItem>
            return entries.values().stream()
                    .map(value -> {
                        try {
                            return objectMapper.readValue((String) value, CartItem.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace(); // Handle JSON processing exception
                            return null;
                        }
                    })
                    .toList();
        } catch (RuntimeException e) {
            e.printStackTrace(); // Handle runtime exception
            return Collections.emptyList();
        }
    }

}
