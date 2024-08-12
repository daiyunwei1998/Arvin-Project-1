package com.stylish.stylish.security;


import com.stylish.stylish.model.Role;
import com.stylish.stylish.model.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;


@Log4j2
@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long accessExpired;// in seconds

    public JwtService (
            @Value("${jwt.access.expired}") long accessExpired
    ) {
        this.secretKey = Jwts.SIG.HS256.key().build();
        this.accessExpired = accessExpired;
    }

    public String createLoginAccessToken(User user) {
        return Jwts.builder()
                .issuer("Arvin")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpired * 1000))
                .subject(user.getEmail())
                .claim("provider", user.getProvider())
                .claim("name", user.getUsername())
                .claim("email", user.getEmail())
                .claim("picture", user.getPicture())
                .claim("role", user.getRole())
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaimsFromToken(String token) throws JwtException{
        Claims claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaimsFromToken(token).getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean isTokenValid(String token, User user) {
        final String username = getUserFromToken(token).getUsername();
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public User getUserFromToken(String token) {

        if (isTokenExpired(token)) {
            log.warn("expired!");
            throw new JwtException("Expired Token");
        }

        Claims claims = getClaimsFromToken(token);
        // ! do not use Map.of() in case of null value
        User user = User.builder().build();
        user.setProvider(claims.get("provider", String.class));
        user.setName(claims.get("name", String.class));
        user.setEmail(claims.get("email", String.class));
        user.setPicture(claims.get("picture", String.class));
        // Convert role string to Role enum
        String roleString = claims.get("role", String.class);
        if (roleString != null) {
            user.setRole(Role.valueOf(roleString.toUpperCase()));
        }


        return  user;
    }
}