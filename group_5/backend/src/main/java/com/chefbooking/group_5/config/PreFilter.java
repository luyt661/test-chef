package com.chefbooking.group_5.config;

import com.chefbooking.group_5.service.JwtService;
import com.chefbooking.group_5.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Không có header hoặc không phải Bearer → bỏ qua (public API)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found. Passing to next filter.");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Lấy token
        final String token = authHeader.substring(7);
        String email = null;

        // 3. Extract email từ token
        try {
            email = jwtService.extractUsername(token);
            log.debug("Extracted email from token: {}", email);
        } catch (Exception e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            sendUnauthorized(response, "Invalid JWT token format");
            return;
        }

        // 4. Kiểm tra: email có & chưa auth
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = userService.getUserDetailsService().loadUserByUsername(email);
                log.debug("Loaded UserDetails for: {}", email);

                // 5. Validate token
                if (jwtService.validate(token, userDetails)) {
                    log.info("Token valid. Authenticating user: {}", email);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } else {
                    log.warn("Token validation failed for user: {}", email);
                    sendUnauthorized(response, "Invalid or expired token");
                    return;
                }

            } catch (Exception e) {
                log.error("Error loading user or validating token: {}", e.getMessage());
                sendUnauthorized(response, "Authentication failed");
                return;
            }
        }

        // 6. Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }

    // Helper: Gửi 401 + message rõ ràng
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}");
    }
}