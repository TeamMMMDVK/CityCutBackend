package com.example.citycutbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RequestValidationBeforeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String header = request.getHeader(AUTHORIZATION);
        System.out.println("Her printes header fra doFilter metoden: " + header);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
