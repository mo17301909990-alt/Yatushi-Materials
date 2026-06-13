package com.it.yts_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// CORS相关导入已移除，使用WebConfig和CustomCorsFilter处理CORS

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS配置已移至WebConfig.java和CustomCorsFilter.java中统一管理
    // 避免多个CORS配置冲突
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() { ... }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())  // 禁用Spring Security的CORS，使用WebConfig和CustomCorsFilter
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 临时允许所有请求，用于测试
                );

        return http.build();
    }
}