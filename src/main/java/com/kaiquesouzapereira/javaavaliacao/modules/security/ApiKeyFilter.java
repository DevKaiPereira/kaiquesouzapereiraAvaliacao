package com.kaiquesouzapereira.javaavaliacao.modules.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.secret}")
    private String apiSecret;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();


        return path.startsWith("/users")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        String requestApiKey = request.getHeader("X-Api-Key");
        String requestApiSecret = request.getHeader("X-Api-Secret");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        boolean isApiKeyValid = Objects.equals(apiKey, requestApiKey);
        boolean isApiSecretValid = Objects.equals(apiSecret, requestApiSecret);

        if (isApiKeyValid && isApiSecretValid) {
            var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("API_USER", null, java.util.Collections.emptySet());
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Invalid or missing API key.\"}");
        }
    }
}