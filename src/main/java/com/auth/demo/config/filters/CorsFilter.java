package com.auth.demo.config.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(11)
public class CorsFilter implements Filter {

    public CorsFilter() {
        super();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equals(request.getMethod()))
            response.setStatus(HttpServletResponse.SC_OK);
        else
            chain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig filterConfig) {
        // need init but no need any implementation
    }

    @Override
    public void destroy() {
        // need destroy but no need any implementation
    }

}
