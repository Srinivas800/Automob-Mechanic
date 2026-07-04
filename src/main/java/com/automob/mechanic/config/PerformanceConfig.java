package com.automob.mechanic.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Configuration
public class PerformanceConfig {

    private static final Logger log = Logger.getLogger(PerformanceConfig.class.getName());

    // ── Measures response time for every API call and logs it ────────
    @Bean
    public OncePerRequestFilter apiLatencyFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain)
                    throws ServletException, IOException {

                if (request.getRequestURI().startsWith("/api/")) {
                    long start = System.currentTimeMillis();
                    try {
                        chain.doFilter(request, response);
                    } finally {
                        long duration = System.currentTimeMillis() - start;
                        response.setHeader("X-Response-Time", duration + "ms");
                        log.info(String.format("[LATENCY] %s %s → %dms | Status: %d",
                                request.getMethod(),
                                request.getRequestURI(),
                                duration,
                                response.getStatus()));
                    }
                } else {
                    chain.doFilter(request, response);
                }
            }
        };
    }
}
