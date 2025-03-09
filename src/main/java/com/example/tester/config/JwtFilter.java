package com.example.tester.config;

import com.example.tester.utils.JwtUtil;
import com.example.tester.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            System.out.println("🔹 Incoming Request: " + request.getMethod() + " " + request.getRequestURI());

            // 🔥 Izinkan request OPTIONS agar tidak terblokir
            if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
                System.out.println("✅ OPTIONS request allowed");
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            // 🔥 Melewati filter jika request menuju /auth/**
            if (request.getRequestURI().startsWith("/auth/")) {
                System.out.println("✅ Request ke /auth/, filter dilewati");
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            System.out.println("🔹 Authorization Header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                System.out.println("🔹 Extracted Token: " + token);
                
                String username = jwtUtil.extractUsername(token);
                System.out.println("🔹 Extracted Username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("🔹 User found: " + userDetails.getUsername());

                    if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                        System.out.println("✅ Token Valid!");

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        System.out.println("❌ Token Invalid!");
                    }
                } else {
                    System.out.println("❌ Username in token is null or already authenticated");
                }
            } else {
                System.out.println("❌ Authorization header missing or invalid format");
            }

            filterChain.doFilter(request, response);
        }
}
