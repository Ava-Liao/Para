package com.graduate.para.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许cookies跨域
        config.setAllowCredentials(true);
        
        // 允许向该服务器提交请求的URI
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // 允许访问的头信息
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // 允许提交请求的方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 暴露头信息
        config.setExposedHeaders(Arrays.asList("Authorization", "token"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 