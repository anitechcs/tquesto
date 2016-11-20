package com.anitech.tquesto.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring user details service implementation
 * 
 * @author Tapas
 *
 */
@Service
public class AuthenticationUserDetailService implements UserDetailsService {

	private final Logger LOG = LoggerFactory.getLogger(AuthenticationUserDetailService.class);
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		LOG.debug("Inside AuthenticationUserDetailService->loadUserByUsername() method!");
		UserDetails user = new User("admin", "password", null);
		
		//TODO: Implementation pending
		return user;
	}

}
