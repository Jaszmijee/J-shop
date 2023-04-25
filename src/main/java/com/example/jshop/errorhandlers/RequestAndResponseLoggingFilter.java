package com.example.jshop.errorhandlers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        val cachedHttpServletRequest = new ContentCachingRequestWrapper(request);
        val cachedHttpServletResponse = new ContentCachingResponseWrapper(response);
        var logs = new StringBuilder();
        try {
            filterChain.doFilter(cachedHttpServletRequest, cachedHttpServletResponse);
            String path = getRequestedPath(cachedHttpServletRequest);
            logs.append("REQUEST PATH: {} ").append(path).append("\n" + "REQUEST DATA: {} ");
            if ((path.contains("login") || path.contains("customer"))) {
                logs.append("sensitive data");
            } else {
                logs.append(new String(cachedHttpServletRequest.getContentAsByteArray(), StandardCharsets.UTF_8));
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        } finally {
            logs.append("\n" + "RESPONSE STATUS: {} ").append(cachedHttpServletResponse.getStatus())
                    .append("\n" + "RESPONSE DATA: {} ").append(new String(cachedHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
            log.info(logs.toString());
        }
        cachedHttpServletResponse.copyBodyToResponse();
    }

    private static String getRequestedPath(HttpServletRequest request) {
        val queryString = request.getQueryString();
        if (queryString == null) {
            return request.getMethod() + " " + request.getRequestURI();
        } else {
            return (request.getMethod() + " " + request.getRequestURI() + "?" + queryString);
        }
    }
}
