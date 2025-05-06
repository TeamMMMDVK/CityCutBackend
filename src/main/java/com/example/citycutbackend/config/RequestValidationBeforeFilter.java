package com.example.citycutbackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

//public class RequestValidationBeforeFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String header = request.getHeader(AUTHORIZATION);
//        System.out.println("Her printes header fra doFilter metoden: " + header);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
