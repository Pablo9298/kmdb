package com.pablo9298.kmdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class KmdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmdbApplication.class, args);
    }

    @Bean // Indicates this method produces a bean to be managed by Spring
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) { // Configure CORS settings to allow all origins for specified HTTP methods
                registry.addMapping("/**") // Allow CORS on all paths
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");  // Allowed HTTP methods
            }
        };
    }

}
