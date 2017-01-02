package com.anitech.tquesto.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.anitech.tquesto.util.TquestoProperties;

/**
 * This filter is responsible for providing CORS support
 * 
 * @author Tapas
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {

	private final Logger LOG = LoggerFactory.getLogger(CorsFilter.class);

	@Inject
    private TquestoProperties tquestoProperties;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		LOG.debug("Inside CorsFilter->doFilterInternal() method!");
		//response.setHeader("Access-Control-Allow-Origin", tquestoProperties.getCors().getAllowedOrigins());
		//response.setHeader("Access-Control-Allow-Methods", tquestoProperties.getCors().getAllowedMethods());
		//response.setHeader("Access-Control-Max-Age", tquestoProperties.getCors().getMaxAge());
		//response.setHeader("Access-Control-Allow-Headers", tquestoProperties.getCors().getAllowedHeaders());
		
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(request, response);
		}
		
	}
	
}
