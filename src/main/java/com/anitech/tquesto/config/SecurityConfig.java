package com.anitech.tquesto.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.anitech.tquesto.auth.AuthenticationTokenFilter;
import com.anitech.tquesto.auth.RestAuthenticationEntryPoint;
import com.anitech.tquesto.util.Constants;

/**
 * Spring Security configuration
 * 
 * @author Tapas
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
		LOG.debug("Inside SecurityConfig->configureGlobal() method!");
		authBuilder
			.userDetailsService(userDetailsService)
			.passwordEncoder(bcryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOG.debug("Inside SecurityConfig->configure() method!");
		
		http
			.csrf()
			.disable()
			.httpBasic()
		.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
		.and()
			.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow CORS preflight request
			.antMatchers("/assets/**").permitAll()
			.antMatchers("/api/authenticate").permitAll()
			.antMatchers(Constants.AUTH_SECURITY_CONTEXT).permitAll();
		
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationTokenFilter authenticationTokenFilter() throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
		authenticationTokenFilter.setFilterProcessesUrl(Constants.AUTH_SECURITY_CONTEXT);
		return authenticationTokenFilter;
	}
	
}
