package com.fbs.app.utils;

import com.fbs.app.service.CustomUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserService customUserService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserService customUserService) {
        this.jwtUtil = jwtUtil;
        this.customUserService= customUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // typical header: Authorization: Bearer <token>
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                if (jwtUtil.isTokenValid(token)) {
                   // username = jwtUtil.getUsernameFromToken(token);
                }
            } catch (Exception e) {
                // log or ignore; token invalid
            }
        }

        // if username found & no authentication present in context — set it
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = customUserService.loadUserByUsername(username);
            if (jwtUtil.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}

