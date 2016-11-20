package com.anitech.tquesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data source configuration
 * 
 * @author Tapas
 *
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	
	@Bean
	public JpaTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
	
}
