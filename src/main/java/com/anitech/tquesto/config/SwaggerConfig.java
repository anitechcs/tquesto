package com.anitech.tquesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 API documentation configuration
 * 
 * @author Tapas
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.regex("/api/.*"))                          
          .build()
          .apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
                .title("TQuesto REST Services Documentation")
                .description("Make sure you are passing a token to access secured services")
                .termsOfServiceUrl("http://www.tquesto.com")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/anitechcs/tquesto/blob/master/LICENSE")
                .version("0.0.1")
                .build();
	}
	
}
