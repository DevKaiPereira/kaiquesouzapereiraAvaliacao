package com.kaiquesouzapereira.javaavaliacao.modules.security;

import com.kaiquesouzapereira.javaavaliacao.modules.providers.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null){
            var token = jwtProvider.verifyToken(header);
            if(token == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            request.setAttribute("userId", token.getSubject());
            var roles = token.getClaim("roles").asList(Object.class);
            var grants = roles.stream().map((role) -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())).toList();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token.getClaim("name").asString(), null, grants);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
