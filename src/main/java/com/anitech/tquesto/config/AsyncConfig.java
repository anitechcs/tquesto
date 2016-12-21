package com.anitech.tquesto.config;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.anitech.tquesto.util.TquestoProperties;

/**
 * @author Tapas
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig implements AsyncConfigurer {

	private final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Inject
    private TquestoProperties tquestoProperties;

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(tquestoProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(tquestoProperties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(tquestoProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix("Tquesto-Async-Executor-");
        return null;//new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
    
}
