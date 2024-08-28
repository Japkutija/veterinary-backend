package com.Japkutija.veterinarybackend.veterinary.security.filters;

import com.Japkutija.veterinarybackend.veterinary.advice.ApiErrorResponse;
import com.Japkutija.veterinarybackend.veterinary.exception.JwtTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.JwtTokenMalformedException;
import com.Japkutija.veterinarybackend.veterinary.exception.JwtTokenMissingException;
import com.Japkutija.veterinarybackend.veterinary.service.impl.CustomUserDetailsService;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    /**
     * Filters each request to validate the JWT token and set the authentication in the security context.
     * If a valid token is found, it sets the user's authentication details in the security context.
     * If the token is expired, an error is logged.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws ServletException if an error occurs during the filtering process
     * @throws IOException      if an I/O error occurs during the filtering process
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final var authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt;

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Displaying the authorization header: {}", authorizationHeader);
            log.warn("JWT Token is missing or invalid");
            chain.doFilter(request, response);
            return;
        }
        jwt = authorizationHeader.substring(7);
        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            log.error("JWT Token has expired");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired", request.getRequestURI());
            return;
        } catch (MalformedJwtException e) {
            log.error("JWT Token is malformed");
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "JWT Token is malformed", request.getRequestURI());
            return;
        } catch (IllegalArgumentException e) {
            log.error("JWT Token is null or empty");
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "JWT Token is null or empty", request.getRequestURI());
            return;
        }

        // Validate token and set authentication
        // The reason we are checking for getAuthentication is to avoid re-authenticating the user
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        var userDetails = this.userDetailsService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails.getUsername()))) {
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continue the filter chain, pass the request and response to the next filter
        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message, String path) throws IOException {
        var apiErrorResponse = new ApiErrorResponse(
                status,
                HttpStatus.valueOf(status).getReasonPhrase(),
                message,
                path
        );
        response.setStatus(status);
        response.setContentType("application/json");
        // Write the text message of the response as JSON
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }

}
