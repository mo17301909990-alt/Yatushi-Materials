package com.it.yts_project.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

    @Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // 动态设置允许的来源
        String origin = request.getHeader("Origin");
        if (origin != null && (
            origin.equals("http://localhost:5173") ||          // 开发环境
            origin.equals("http://120.26.101.0") ||            // 生产环境
            origin.equals("http://120.26.101.0:80") ||         // 生产环境80端口
            origin.equals("http://120.26.101.0:8080") ||       // 生产环境8080端口
            origin.equals("http://120.26.101.0:3000")          // 生产环境3000端口
        )) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            // 如果没有Origin头或不在允许列表中，设置默认值
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
}