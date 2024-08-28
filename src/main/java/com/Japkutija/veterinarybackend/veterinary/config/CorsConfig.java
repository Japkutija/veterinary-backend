package com.Japkutija.veterinarybackend.veterinary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) settings.
 */
@Configuration
public class CorsConfig {

    /**
     * Creates a CorsFilter bean to handle CORS requests.
     * <p>
     * This method configures the CORS settings to allow requests from the specified origin,
     * with all headers and methods allowed, and credentials support enabled.
     *
     * @return a CorsFilter configured with the specified CORS settings
     */
    @Bean
    public CorsFilter corsFilter() {
        var config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Allow the frontend origin
        config.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all methods (POST, GET, etc.)

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS settings to all endpoints

        return new CorsFilter(source);
    }
}