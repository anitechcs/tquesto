package com.anitech.tquesto.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.anitech.tquesto.auth.HttpAuthenticationEntryPoint;
import com.anitech.tquesto.auth.JwtConfigurer;
import com.anitech.tquesto.auth.TokenProvider;
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
	private HttpAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Inject
    private TokenProvider tokenProvider;
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
		LOG.debug("Inside SecurityConfig->configureGlobal() method!");
		try {
			authBuilder
				.userDetailsService(userDetailsService)
				.passwordEncoder(bcryptPasswordEncoder());
		} catch (Exception e) {
			throw new BeanInitializationException("Security configuration failed", e);
		}
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOG.debug("Inside SecurityConfig->configure() method!");
		
		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
		.and()
			.csrf()
			.disable()
			.httpBasic()
		.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //Allow CORS preflight request
            .antMatchers("/*.{css,js,html}").permitAll()
            .antMatchers("/assets/**").permitAll()
			.antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(Constants.ADMIN)
		.and()
            .apply(securityConfigurerAdapter());
		
	}
	
	@Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(tokenProvider);
    }
	
}
