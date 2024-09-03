package ru.evito.evito.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            var jwt = authHeader.substring(7);
            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid JWT Token in Bearer Header");
            }
            else {
                try {
                    var username = jwtService.validateAndVerifyToken(jwt);
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    var token = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(), userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                } catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "Invalid JWT Token");
                } catch (UsernameNotFoundException exc) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "User not found");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
