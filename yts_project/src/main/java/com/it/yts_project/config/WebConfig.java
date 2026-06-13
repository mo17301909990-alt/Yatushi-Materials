package com.it.yts_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",          // 开发环境
                    "http://120.26.101.0",            // 生产环境
                    "http://120.26.101.0:80",         // 生产环境80端口
                    "http://120.26.101.0:8080",       // 生产环境8080端口
                    "http://120.26.101.0:3000"        // 生产环境3000端口
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}