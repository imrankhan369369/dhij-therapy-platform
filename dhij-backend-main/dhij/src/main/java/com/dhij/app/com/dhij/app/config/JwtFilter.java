
package com.dhij.app.com.dhij.app.config;

import java.io.IOException; // ✅ Use java.io.IOException

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // ✅ to set details
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dhij.app.com.dhij.app.model.User;
import com.dhij.app.com.dhij.app.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException { // ✅ java.io.IOException

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            String username = null;
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception ex) {
                // Optional: log and short-circuit with 401/invalid token message
                // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // return;
            }

            // Only authenticate if no existing authentication is present
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(username)
                        .orElse(null);

                if (user != null && jwtUtil.validateToken(token, user)) {
                    // Build authorities (ensure role format matches your security config)
                    String role = user.getRole(); // e.g., "ADMIN" or "ROLE_ADMIN"
                    List<SimpleGrantedAuthority> authorities = (role == null || role.isBlank())
                            ? Collections.emptyList()
                            // If your config uses hasRole("ADMIN"), Spring expects authority "ROLE_ADMIN":
                            : List.of(new SimpleGrantedAuthority(
                                    role.startsWith("ROLE_") ? role : "ROLE_" + role
                              ));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

