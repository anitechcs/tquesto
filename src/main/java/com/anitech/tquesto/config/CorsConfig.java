package com.anitech.tquesto.config;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.anitech.tquesto.util.TquestoProperties;

/**
 * CORS configuration
 * 
 * @author Tapas
 *
 */
@Configuration
public class CorsConfig {

	@Inject
    private TquestoProperties tquestoProperties;
	
	@Bean
    @ConditionalOnProperty(name = "tquesto.cors.allowed-origins")
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = tquestoProperties.getCors();
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/v2/api-docs", config);
        return new CorsFilter(source);
    }
	
}
