package com.example.jshop.security;

import camundajar.impl.com.google.gson.Gson;
import com.example.jshop.cartsandorders.controller.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public AuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (!requiresAuthentication(request, response)) {
            super.doFilter(req, res, chain);
        } else {
            super.doFilter(new ContentCachingRequestWrapper(request), res, chain);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        val loginRequest = getLoginRequest(request);
        val authRequest = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    public static LoginRequest getLoginRequest(HttpServletRequest request) {
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            Gson gson = new Gson();
            if (!request.getInputStream().isFinished()) {
                return gson.fromJson(reader, LoginRequest.class);
            } else {
                return gson.fromJson(
                        new InputStreamReader(new ByteArrayInputStream(((ContentCachingRequestWrapper) request).getContentAsByteArray())),
                        LoginRequest.class
                );
            }
        } catch (IOException ex) {
            log.warn("Get authentication request error", ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                log.warn("Closing reader error", ex);
            }
        }
    }
}
