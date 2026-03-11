package com.resume.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "https://resume-frontend-cybercrime.netlify.app",
                "https://dreamy-cranachan-baac8b.netlify.app",        // For local Vite dev
                "http://localhost:3000",         // For local React dev
                "https://your-frontend.vercel.app" // If also on Vercel
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "Content-Type")
            .allowCredentials(true)
            .maxAge(3600); // Cache preflight for 1 hour
    }
}