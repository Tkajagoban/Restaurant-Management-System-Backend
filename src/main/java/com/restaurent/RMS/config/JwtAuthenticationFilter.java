package com.restaurent.RMS.config;

import com.restaurent.RMS.entities.Token;
import com.restaurent.RMS.repositories.TokenRepository;
import com.restaurent.RMS.services.CustomUserDetailsService;
import com.restaurent.RMS.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String userEmail = null;

        try {
            // Extract email from token
            userEmail = jwtService.extractUsername(jwt);

            // Check revocation
            Token tokenEntity = tokenRepository.findByToken(jwt).orElse(null);
            if (tokenEntity == null) {
                logger.warn("Token not found in database: {}", jwt);
                filterChain.doFilter(request, response);
                return;
            }

            if (tokenEntity.getRevoked()) {
                logger.warn("Token is revoked: {}", jwt);
                filterChain.doFilter(request, response);
                return;
            }

            // Authenticate user
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.validateToken(jwt, userEmail)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication to Spring context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("DEBUG: Authentication set for user: {} with authorities: {}", userEmail,
                            userDetails.getAuthorities());
                } else {
                    logger.warn("DEBUG: Token validation failed for user: {}", userEmail);
                }
            } else if (userEmail != null) {
                logger.debug("DEBUG: Authentication already exists in context for user: {}", userEmail);
            }

        } catch (Exception e) {
            // DO NOT set attributes → This avoids "Full authentication required" error
            logger.error("JWT Error: {}", e.getMessage());
            // Allow request to continue. Do not block.
        }

        filterChain.doFilter(request, response);
    }
}