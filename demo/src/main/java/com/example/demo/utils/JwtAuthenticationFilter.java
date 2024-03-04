package com.example.demo.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "aaa123456";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isSkipped(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "權限不足");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "權限不足");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isSkipped(HttpServletRequest request) {
        String[] skipPaths = {"/auth/login", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html"};
        for (String path : skipPaths) {
            if (pathMatcher.match(path, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }
}
