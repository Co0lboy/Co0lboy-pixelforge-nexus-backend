// File: src/main/java/com/pixelforge/nexus/security/JwtAuthenticationFilter.java
package com.pixelforge.nexus.security;

import com.pixelforge.nexus.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            // --- DEBUGGING ---
            System.out.println("====== JWT AUTH FILTER ======");
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("JWT Token from Header: " + jwt);
            // --- END DEBUGGING ---

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // --- DEBUGGING ---
                System.out.println("Username from Token: " + username);
                // --- END DEBUGGING ---

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // --- DEBUGGING ---
                System.out.println("Authorities from UserDetailsService: " + userDetails.getAuthorities());
                // --- END DEBUGGING ---

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // --- DEBUGGING ---
                System.out.println("User successfully authenticated and set in SecurityContext.");
                // --- END DEBUGGING ---
            } else {
                System.out.println("Could not validate JWT token.");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        System.out.println("============================");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
