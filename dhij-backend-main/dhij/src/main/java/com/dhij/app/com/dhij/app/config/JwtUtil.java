
// com.dhij.app.com.dhij.app.config.JwtUtil.java
package com.dhij.app.com.dhij.app.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import com.dhij.app.com.dhij.app.model.User;

@Component
public class JwtUtil {

    // Store secret in application.properties and inject it instead of hardcoding
    private final Key key = Keys.hmacShaKeyFor("change-this-to-a-very-long-secret-key-min-256-bits".getBytes());
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    public String generateToken(User user) {
        String username = user.getUsername();
        String dbRole = user.getRole(); // e.g., "USER", "ADMIN", or "ROLE_USER"
        String normalizedRole = (dbRole == null ? "USER" : dbRole.replaceFirst("^ROLE_+", "").toUpperCase());

        return Jwts.builder()
                .setSubject(username)
                .claim("role", normalizedRole) // keep plain role in claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Object role = parseClaims(token).get("role");
        return role == null ? null : role.toString();
    }

    public boolean validateToken(String token, User user) {
        try {
            Claims claims = parseClaims(token);
            String username = claims.getSubject();
            Date exp = claims.getExpiration();
            return username != null
                    && username.equals(user.getUsername())
                    && exp != null
                    && exp.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
