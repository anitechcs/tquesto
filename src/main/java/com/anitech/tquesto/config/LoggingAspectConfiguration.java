package com.anitech.tquesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import com.anitech.tquesto.aop.LoggingAspect;
import com.anitech.tquesto.util.Constants;

/**
 * AOP logging execution configuration
 * 
 * @author Tapas
 *
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

	@Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
	
}
