package com.tradin.common.filter;

import com.tradin.common.exception.TradinException;
import com.tradin.common.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.tradin.common.exception.ExceptionMessage.EMPTY_HEADER_EXCEPTION;
import static com.tradin.common.exception.ExceptionMessage.INVALID_BEARER_FORMAT_EXCEPTION;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> ALLOW_LIST = List.of("/swagger-ui", "/api-docs", "/health-check", "/signUp",
            "/signIn");

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!isAllowList(request.getRequestURI())) {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER_PREFIX);
            Long userId = validateHeaderAndGetUserId(bearerToken);
            setAuthentication(userId);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowList(String requestURI) {
        return ALLOW_LIST.stream().anyMatch(requestURI::contains);
    }

    private Long validateHeaderAndGetUserId(String bearerToken) {
        validateHasText(bearerToken);
        validateStartWithBearer(bearerToken);
        return validateAccessToken(getAccessTokenFromBearer(bearerToken));
    }

    private void validateHasText(String bearerToken) {
        if (!StringUtils.hasText(bearerToken)) {
            throw new TradinException(EMPTY_HEADER_EXCEPTION);
        }
    }

    private void validateStartWithBearer(String bearerToken) {
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            throw new TradinException(INVALID_BEARER_FORMAT_EXCEPTION);
        }
    }

    private Long validateAccessToken(String accessToken) {
        return jwtUtil.validateAccessToken(accessToken);
    }

    private String getAccessTokenFromBearer(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }

    private void setAuthentication(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(userId));
    }
}
