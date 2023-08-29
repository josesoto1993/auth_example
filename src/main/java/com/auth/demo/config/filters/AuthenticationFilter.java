package com.auth.demo.config.filters;

import com.auth.demo.token.TokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(12)
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    @Autowired
    private final TokenService tokenService;

    @Value("${token.jwt.request-header}")
    private String tokenHeader;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(tokenHeader);
        if (isAnUserRequest(req)) {
            tokenService.checkIfCanAccessUser(token);
        }
        if (isAnAdminRequest(req)) {
            tokenService.checkIfCanAccessAdmin(token);
        }
        chain.doFilter(request, response);
    }

    private boolean isAnUserRequest(HttpServletRequest req) {
        return req.getRequestURI().contains("/user-area");
    }

    private boolean isAnAdminRequest(HttpServletRequest req) {
        return req.getRequestURI().contains("/admin-area");
    }
}
