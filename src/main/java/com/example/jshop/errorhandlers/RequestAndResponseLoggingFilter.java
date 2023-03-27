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
        try {
            filterChain.doFilter(cachedHttpServletRequest, cachedHttpServletResponse);
            String path = getRequestedPath(cachedHttpServletRequest);
            if (path.contains("/admin")) {
                path = validatePathInfo(path);
            }
            log.info("REQUEST PATH: {}", path);
            log.info("REQUEST DATA: {}", new String(cachedHttpServletRequest.getContentAsByteArray(), StandardCharsets.UTF_8));
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        } finally {
            log.info("RESPONSE STATUS: {}", cachedHttpServletResponse.getStatus());
            log.info("RESPONSE DATA: {}", new String(cachedHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
            cachedHttpServletResponse.copyBodyToResponse();
        }
    }

    private static String getRequestedPath(HttpServletRequest request) {
        val queryString = request.getQueryString();
        if (queryString == null) {
            return request.getMethod() + " " + request.getRequestURI();
        } else {
            return (request.getMethod() + " " + request.getRequestURI() + "?" + queryString);
        }
    }

    private static String validatePathInfo(String path) {
        String subStringKey = path.substring(path.indexOf("?key=") + 5, path.indexOf("&token"));
        String subStringToken = path.substring(path.indexOf("&token=") + 7);
        if (subStringToken.contains("&")) {
            subStringToken = subStringToken.substring(0, subStringToken.indexOf("&"));
        }
        return path.replace(subStringKey, "xxx").replace(subStringToken, "xxx");
    }

}
