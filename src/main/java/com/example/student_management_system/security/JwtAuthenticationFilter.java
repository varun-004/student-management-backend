package com.example.student_management_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("JWT FILTER EXECUTED");

        String path = request.getServletPath();

        System.out.println("PATH: " + path);

        if (path.startsWith("/auth")) {

            System.out.println("AUTH API - SKIPPING JWT FILTER");

            filterChain.doFilter(request, response);

            return;
        }

        final String authHeader =
                request.getHeader("Authorization");

        final String jwt;
        final String userEmail;

        // CHECK HEADER
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("NO TOKEN FOUND");

            filterChain.doFilter(request, response);

            return;
        }


        // EXTRACT TOKEN
        jwt = authHeader.substring(7);

        System.out.println("TOKEN: " + jwt);

        // EXTRACT EMAIL
        userEmail = jwtService.extractUsername(jwt);

        System.out.println("EMAIL: " + userEmail);

        // CHECK AUTH
        if (userEmail != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(userEmail);

            System.out.println("USER LOADED");

            if (jwtService.isTokenValid(jwt, userDetails)) {

                System.out.println("TOKEN VALID");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);

                System.out.println("AUTHENTICATION SET");
            }
        }

        filterChain.doFilter(request, response);
    }
}